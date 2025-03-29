package omsu.mim.imit.school.registry.buiseness.service;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.AttendanceEntity;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.data.entity.ClassEntity;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import omsu.mim.imit.school.registry.data.repository.AttendanceRepository;
import omsu.mim.imit.school.registry.data.repository.ChildRepository;
import omsu.mim.imit.school.registry.data.repository.ClassRepository;
import omsu.mim.imit.school.registry.data.repository.GroupRepository;
import omsu.mim.imit.school.registry.rest.dto.request.CreateScheduleRequest;
import omsu.mim.imit.school.registry.rest.mapper.GroupExcelMapper;
import omsu.mim.imit.school.registry.util.exception.ObjectNotFoundException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;
    private final ChildRepository childRepository;
    private final ClassRepository classRepository;
    private final AttendanceRepository attendanceRepository;

    public void create(GroupEntity entity) {
        repository.save(entity);
    }

    public GroupEntity update(GroupEntity entity) {
        repository.update(entity);
        return repository.findById(entity.getId()).get();
    }

    public ResponseEntity<Resource> downloadAllGroupsInfo() {
        List<GroupEntity> groupEntities = repository.findAll();
        return convertToResponse(GroupExcelMapper.allGroupsToExcel(groupEntities),
                "groups.xlsx");
    }

    public ResponseEntity<Resource> downloadChildInGroups(List<UUID> groupIds) {
        List<GroupEntity> groupEntities = repository.findAllById(groupIds);
        List<ChildEntity> childEntities = repository.getChildInGroups(groupIds);

        var groupsSet = groupEntities.stream().collect(Collectors.toUnmodifiableSet());

        return convertToResponse(GroupExcelMapper.childInGroupsToExcel(childEntities, groupsSet),
                "child_in_group.xlsx");
    }

    public ResponseEntity<Resource> downloadAllChild() {
        List<ChildEntity> childEntities = childRepository.findAll();
        List<GroupEntity> groupEntities = repository.findAll();

        var groupsByIdMap = groupEntities.stream().collect(Collectors.toMap(GroupEntity::getId, Function.identity()));

        return convertToResponse(GroupExcelMapper.allChildToExcel(childEntities, groupsByIdMap),
                "child.xlsx");
    }

    public ResponseEntity<Resource> downloadChildByDir(String[] dirList) {
        List<ChildEntity> childEntities = childRepository.findAll();
        List<GroupEntity> groupEntities = repository.findAll();

        var dirs = Arrays.stream(dirList).toList();
        var groupsByIdMap = groupEntities.stream().collect(Collectors.toMap(GroupEntity::getId, Function.identity()));

        return convertToResponse(GroupExcelMapper.childByDirToExcel(childEntities, groupsByIdMap, dirs),
                "childByDir.xlsx");
    }

    public List<GroupEntity> findAll() {
        return repository.findAll();
    }

    public GroupEntity findById(UUID groupId) {
        return repository.findById(groupId)
                .orElseThrow(() -> new ObjectNotFoundException("Group with id '%s' is not found".formatted(groupId)));
    }

    public void increaseListener(UUID groupId) {
        repository.increaseListenersAmount(groupId);
    }

    private ResponseEntity<Resource> convertToResponse(ByteArrayInputStream data, String filename) {
        InputStreamResource file = new InputStreamResource(data);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = " + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

    public void createJournal(CreateScheduleRequest request) throws ParseException {
        var group = repository.findById(request.getGroupId()).get();
        var dayOfWeek = group.getDayOfWeek();
        var time = group.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        var date = simpleDateFormat.parse(request.getStartingDate());

        var nearestDate = getDate(dayOfWeek + ", " + time, date);
        var scheduleDateTime = nearestDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        List<ClassEntity> oldClassesInGroup = classRepository.getClassesInGroup(request.getGroupId());
        oldClassesInGroup.forEach(classEntity -> attendanceRepository.deleteAllByClassId(classEntity.getId()));
        classRepository.deleteAllByGroupId(request.getGroupId());

        for (int i = 0; i < request.getClassesAmount(); i++) {
            classRepository.save(new ClassEntity(UUID.randomUUID(), request.getGroupId(), "", scheduleDateTime.plusWeeks(i)));
        }

        List<ChildEntity> childInGroup = childRepository.getAllByGroupId(request.getGroupId());
        List<ClassEntity> classesInGroup = classRepository.getClassesInGroup(request.getGroupId());

        childInGroup
                .forEach(child -> classesInGroup
                        .forEach(classEntity -> createAttendance(child, classEntity))
                );
    }

    private Date getDate(String input, Date date1) {
        long MILLISECONDS_PER_WEEK = 7L * 24 * 60 * 60 * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("E, H:mm");
        Date date = sdf.parse(input, new ParsePosition(0));

        Calendar c = Calendar.getInstance();
        c.setTime(date1);
        int todayDSTOffset = c.get(Calendar.DST_OFFSET);
        int epochDSTOffset = c.get(Calendar.DST_OFFSET);

        long parsedMillis = date.getTime() + (epochDSTOffset - todayDSTOffset);

        long millisInThePast = c.getTimeInMillis() - parsedMillis;
        long weeksInThePast = millisInThePast / MILLISECONDS_PER_WEEK;

        return new Date(parsedMillis + (weeksInThePast + 1) * MILLISECONDS_PER_WEEK);
    }

    private void createAttendance(ChildEntity child, ClassEntity classEntity) {
        var attendance = new AttendanceEntity(UUID.randomUUID(), classEntity.getId(), child.getId(), "", false);
        attendanceRepository.save(attendance);
    }

    public List<ChildEntity> getChildList(UUID groupId) {
        return childRepository.getAllByGroupId(groupId);
    }

    public List<ClassEntity> getClassesList(UUID groupId) {
        return classRepository.getClassesInGroup(groupId);
    }

    public List<AttendanceEntity> getAttendanceInGroup(UUID groupId) {
        var classes = classRepository.getClassesInGroup(groupId);
        return classes
                .stream()
                .flatMap(classEntity -> attendanceRepository.getAllByClassId(classEntity.getId()).stream())
                .toList();
    }

    public AttendanceEntity setAttendance(UUID attendanceId, Boolean isAttend,String comment) {
        attendanceRepository.setAttendance(attendanceId, isAttend, comment);
        return attendanceRepository.findById(attendanceId).get();
    }

    public ClassEntity setTheme(UUID classId, String theme) {
        classRepository.setTheme(classId, theme);
        return classRepository.findById(classId).get();
    }

}