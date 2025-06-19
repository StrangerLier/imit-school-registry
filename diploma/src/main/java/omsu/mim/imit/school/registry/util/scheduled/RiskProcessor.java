package omsu.mim.imit.school.registry.util.scheduled;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.DirectionEntity;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import omsu.mim.imit.school.registry.data.entity.RiskEntity;
import omsu.mim.imit.school.registry.data.entity.TeacherEntity;
import omsu.mim.imit.school.registry.data.repository.DirectionRepository;
import omsu.mim.imit.school.registry.data.repository.GroupRepository;
import omsu.mim.imit.school.registry.data.repository.RiskRepository;
import omsu.mim.imit.school.registry.data.repository.TeacherRepository;
import omsu.mim.imit.school.registry.util.excel.BaseExcel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RiskProcessor {

    private final GroupRepository groupRepository;
    private final RiskRepository riskRepository;
    private final TeacherRepository teacherRepository;
    private final DirectionRepository directionRepository;

    private static final String UNFILLED_GROUP_REASON = "UNFILLED_GROUP";
    private static final String OVERLOADED_TEACHER_REASON = "OVERLOADED_TEACHER";
    private static final String SAME_TIME_GROUPS_REASON = "SAME_TIME_GROUPS";

    private static final BaseExcel baseExcel = new BaseExcel();

    @Scheduled(cron = "0 3,13 * * * *") //at 3am and 13 pm
    public void process() {
        var groups = groupRepository.findAll();
        var teachers = teacherRepository.findAll();

        checkForUnfilledGroups(groups);
        checkForManyGroupsSameTime(groups);
        checkForTeachersOverload(teachers);
    }

    private void checkForUnfilledGroups(List<GroupEntity> groups) {
        for(GroupEntity group : groups) {
            var amountOfChildren = group.getListenersAmount();
            var approved =  group.getApprovedListeners();

            if (amountOfChildren != 0 && (double) approved / amountOfChildren < 0.5) {
                var comment = "Группа заполнена на " + approved + " человек из " + amountOfChildren;
                saveRisk(group.getId(), UNFILLED_GROUP_REASON, comment, null);
            } else {
                deleteRisk(group.getId(), UNFILLED_GROUP_REASON, null);
            }
        }
    }

    private void checkForManyGroupsSameTime(List<GroupEntity> groups) {

        Map<String, List<UUID>> groupsByPartOfDay = new HashMap<>();

        groupsByPartOfDay.put("Понедельник первая половина дня", new ArrayList<>());
        groupsByPartOfDay.put("Понедельник вторая половина дня", new ArrayList<>());
        groupsByPartOfDay.put("Вторник первая половина дня", new ArrayList<>());
        groupsByPartOfDay.put("Вторник вторая половина дня", new ArrayList<>());
        groupsByPartOfDay.put("Среда первая половина дня", new ArrayList<>());
        groupsByPartOfDay.put("Среда вторая половина дня", new ArrayList<>());
        groupsByPartOfDay.put("Четверг первая половина дня", new ArrayList<>());
        groupsByPartOfDay.put("Четверг вторая половина дня", new ArrayList<>());
        groupsByPartOfDay.put("Пятница первая половина дня", new ArrayList<>());
        groupsByPartOfDay.put("Пятница вторая половина дня", new ArrayList<>());
        groupsByPartOfDay.put("Суббота первая половина дня", new ArrayList<>());
        groupsByPartOfDay.put("Суббота вторая половина дня", new ArrayList<>());
        groupsByPartOfDay.put("Воскресенье первая половина дня", new ArrayList<>());
        groupsByPartOfDay.put("Воскресенье вторая половина дня", new ArrayList<>());

        for(GroupEntity group : groups) {
            if (group.getId().equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
                continue;
            }
            var mapKey = group.getDayOfWeek() + " " + extractPartOfDay(group.getTime());
            var array = groupsByPartOfDay.get(mapKey);
            array.add(group.getId());
        }

        for(Map.Entry<String, List<UUID>> entry : groupsByPartOfDay.entrySet()) {
            var groupsIds = entry.getValue();
            if ((double) groupsIds.size() > (double) groups.size() / 4) {
                var comment = "Много групп ведут занятия в " + entry.getKey();
                groupsIds.forEach(groupId -> saveRisk(groupId, SAME_TIME_GROUPS_REASON, comment, null));
            } else {
                groupsIds.forEach(groupId -> deleteRisk(groupId, SAME_TIME_GROUPS_REASON, null));
            }
        }
    }

    private String extractPartOfDay(String groupTime) {
        var hours = Integer.parseInt(groupTime.substring(0, 2));
        return hours < 13 ? "первая половина дня" : "вторая половина дня";
    }

    private void checkForTeachersOverload(List<TeacherEntity> teachers) {
        var directions = directionRepository.findAll();
        for (DirectionEntity direction : directions) {

            var groupsByDir = groupRepository.getByDirectionsIds(List.of(direction.getId()));

            Map<UUID, List<GroupEntity>> groupsByTeacher = groupsByDir
                    .stream()
                    .collect(Collectors.groupingBy(GroupEntity::getTeacherId));

            var overloadTeachers = groupsByTeacher
                    .entrySet()
                    .stream()
                    .filter(entry -> {
                        var groups = entry.getValue();
                        return groups
                                .stream()
                                .filter(group -> Objects.equals(group.getApprovedListeners(), group.getListenersAmount()))
                                .count() == groups.size();
                        })
                    .map(Map.Entry::getKey)
                    .toList();

            var outsideTeachers = groupsByTeacher
                    .entrySet()
                    .stream()
                    .filter(entry -> {
                        var groups = entry.getValue();
                        return groups
                                .stream()
                                .filter(group -> Objects.equals(group.getApprovedListeners(), group.getListenersAmount()))
                                .count() < groups.size();
                    })
                    .map(Map.Entry::getKey)
                    .toList();
            if (!outsideTeachers.isEmpty()) {
                overloadTeachers.forEach(teacherId -> {
                    var teacher = baseExcel.getByIdFromList(teachers, teacherId);
                    var comment = "У " + teacher.getSurname() + " " + teacher.getName()
                            + " заполнены все группы по направлению " + direction.getName() + " , еще есть свободные преподаватели";
                    saveRisk(teacherId, OVERLOADED_TEACHER_REASON, comment, direction.getId());
                });
            }

            var teachersIds = teachers.stream().map(TeacherEntity::getId).toList();

            var currentRisks = riskRepository.findByGroupIds(teachersIds);

            currentRisks.forEach(risk -> {
                if (!overloadTeachers.contains(risk.getGroupId())) {
                    deleteRisk(risk.getGroupId(), OVERLOADED_TEACHER_REASON, direction.getId());
                }
            });
        }
    }

    private void saveRisk(UUID groupId, String reason, String comment, UUID supportId) {
        var risk = RiskEntity
                .builder()
                .id(UUID.randomUUID())
                .reason(reason)
                .groupId(groupId)
                .comment(comment)
                .supportId(supportId)
                .build();

        var risks = riskRepository.findByGroupIds(List.of(risk.getGroupId()))
                .stream()
                .filter(r -> r.getReason().equals(reason))
                .toList();

        if(risks.isEmpty()) {
            riskRepository.save(risk);
        }
    }

    private void deleteRisk(UUID groupId, String reason, UUID supportId) {
        var risks = riskRepository.findByGroupIds(List.of(groupId));
        var risksToDelete = risks.stream()
                .filter(r -> r.getReason().equals(reason))
                .filter(r -> Objects.isNull(supportId) || r.getSupportId().equals(supportId))
                .toList();
        risksToDelete.forEach(r -> riskRepository.deleteById(r.getId()));

    }
}
