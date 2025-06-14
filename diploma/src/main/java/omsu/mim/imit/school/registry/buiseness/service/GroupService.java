package omsu.mim.imit.school.registry.buiseness.service;

import java.io.ByteArrayInputStream;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.*;
import omsu.mim.imit.school.registry.data.entity.enumeration.GroupStatus;
import omsu.mim.imit.school.registry.data.entity.xml.HolidayEntity;
import omsu.mim.imit.school.registry.data.repository.*;
import omsu.mim.imit.school.registry.rest.dto.request.AddContractInfoRequest;
import omsu.mim.imit.school.registry.rest.dto.request.CreateScheduleRequest;
import omsu.mim.imit.school.registry.rest.dto.response.ChildAttendancesRestResponse;
import omsu.mim.imit.school.registry.rest.dto.response.JournalRestResponse;
import omsu.mim.imit.school.registry.rest.mapper.*;
import omsu.mim.imit.school.registry.util.exception.ObjectNotFoundException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static java.util.function.Predicate.not;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;
    private final ChildRepository childRepository;
    private final ClassRepository classRepository;
    private final TeacherRepository teacherRepository;
    private final DirectionRepository directionRepository;
    private final AttendanceRepository attendanceRepository;
    private final AssistantRepository assistantRepository;
    private final ContractRepository contractRepository;
    private final HolidayRepository holidayRepository;

    private final GroupExcelMapper groupExcelMapper;

    private final ClassRestResponseMapper classRestResponseMapper;
    private final AttendanceRestResponseMapper attendanceRestResponseMapper;

    public void create(GroupEntity entity) {
        repository.save(entity);
    }

    public GroupEntity update(GroupEntity entity) {
        repository.update(entity);
        return repository.findById(entity.getId()).get();
    }

    public ResponseEntity<Resource> downloadAllGroupsInfo() {
        return convertToResponse(groupExcelMapper.allGroupsToExcel(),
                "groups.xlsx");
    }

    public ResponseEntity<Resource> downloadChildInGroups(List<UUID> groupIds) {
        return convertToResponse(groupExcelMapper.childInGroupsToExcel(groupIds),
                "child_in_group.xlsx");
    }

    public ResponseEntity<Resource> downloadAllChild() {
        return convertToResponse(groupExcelMapper.allChildToExcel(),
                "children.xlsx");
    }

    public ResponseEntity<Resource> downloadChildByDir(List<UUID> dirIds) {
        return convertToResponse(groupExcelMapper.childByDirToExcel(dirIds),
                "childByDir.xlsx");
    }

    public ResponseEntity<Resource> downloadJournalForGroups(List<UUID> groupIds) {
        return convertToResponse(groupExcelMapper.journalToExcel(groupIds),
                "journal.xlsx");
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
        System.out.println(STR."DayOfWeek: \{dayOfWeek}");
        var time = group.getTime();
        System.out.println(STR."Time: \{time}");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        var holidays = holidayRepository.findAll()
                .stream()
                .map(HolidayEntity::getHoliday)
                .toList();

        var date = simpleDateFormat.parse(request.getStartingDate());

        var nearestDate = getDate(dayOfWeek + ", " + time, date);
        var scheduleDateTime = nearestDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        List<ClassEntity> oldClassesInGroup = classRepository.getClassesInGroup(request.getGroupId());
        oldClassesInGroup.forEach(classEntity -> attendanceRepository.deleteAllByClassId(classEntity.getId()));
        classRepository.deleteAllByGroupId(request.getGroupId());

        var classesAmount = request.getClassesAmount();

        for (int i = 0; i < classesAmount; i++) {
            if (holidays.contains(scheduleDateTime.plusWeeks(i).toLocalDate())) {
                classesAmount++;
                continue;
            }
            var classToSave = ClassEntity.builder()
                    .id(UUID.randomUUID())
                    .groupId(request.getGroupId())
                    .theme("")
                    .classDateTime(scheduleDateTime.plusWeeks(i))
                    .build();
            classRepository.save(classToSave);
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
        System.out.println(STR."parsed date: \{date.toString()}");

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
        var attendance = AttendanceEntity.builder()
                .id(UUID.randomUUID())
                .classId(classEntity.getId())
                .childId(child.getId())
                .comment(null)
                .build();
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

    public GroupEntity addAssistants(UUID groupId, List<UUID> assistantsIds) {
        var group = repository.findById(groupId).get();

        var groupAssistants = group.getAssistantsIds();

        List<UUID> currentAssistants;

        if(groupAssistants == null || groupAssistants.isEmpty()) {
            currentAssistants = List.of();
        } else {
            currentAssistants = Arrays.stream(group.getAssistantsIds().split(";")).map(UUID::fromString).toList();
        }

        var finalAssistants = Stream.concat(assistantsIds.stream(), currentAssistants.stream())
                .distinct()
                .toList();

        var finalAssistantsStr = finalAssistants
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));

        repository.updateAssistants(groupId, finalAssistantsStr);
        return repository.findById(groupId).get();
    }

    public GroupEntity removeAssistants(UUID groupId, List<UUID> assistantsIds) {
        var group = repository.findById(groupId).get();

        var groupAssistants = group.getAssistantsIds();

        List<UUID> currentAssistants;

        if(groupAssistants == null || groupAssistants.isEmpty()) {
            currentAssistants = List.of();
        } else {
            currentAssistants = Arrays.stream(group.getAssistantsIds().split(";")).map(UUID::fromString).toList();
        }

        var finalAssistantsStr = currentAssistants
                .stream()
                .filter(not(assistantsIds::contains))
                .map(String::valueOf)
                .collect(Collectors.joining(";"));

        repository.updateAssistants(groupId, finalAssistantsStr);
        return repository.findById(groupId).get();
    }


    public List<AssistantEntity> getAssistants(UUID groupId) {

        var group = repository.findById(groupId).get();

        var groupAssistants = group.getAssistantsIds();

        List<UUID> currentAssistants;

        if(groupAssistants == null || groupAssistants.isEmpty()) {
            currentAssistants = List.of();
        } else {
            currentAssistants = Arrays.stream(group.getAssistantsIds().split(";")).map(UUID::fromString).toList();
        }

        return assistantRepository.findAllById(currentAssistants);
    }

    public ContractEntity getContractByChild(UUID childId) {
        return contractRepository.getByChildId(childId);
    }

    public ClassEntity changeClassDate(UUID classId, String newDate) {
        var newLocalDate = strToLocalDate(newDate);

        var oldClass = classRepository.findById(classId).get();

        var oldTime = oldClass.getClassDateTime().toLocalTime();

        classRepository.setDate(classId,  newLocalDate.atTime(oldTime));

        return classRepository.findById(classId).get();
    }

    private LocalDate strToLocalDate(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        LocalDate date = null;
        try {
             date = simpleDateFormat.parse(str).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException e){
            e.printStackTrace();
        }
        return date;
    }

    public ClassEntity addClass(UUID groupId, String classDate) {
        var date = strToLocalDate(classDate);

        var group = repository.findById(groupId).get();

        var newUuid = UUID.randomUUID();

        var classToSave = ClassEntity.builder()
                        .id(newUuid)
                        .groupId(groupId)
                        .theme("")
                        .classDateTime(date.atTime(LocalTime.parse(group.getTime())))
                        .build();

        classRepository.save(classToSave);

        List<ChildEntity> childInGroup = childRepository.getAllByGroupId(groupId);
        List<ClassEntity> classesInGroup = classRepository.getClassesInGroup(groupId);

        childInGroup
                .forEach(child -> classesInGroup
                        .forEach(classEntity -> createAttendance(child, classEntity))
                );

        return classRepository.findById(newUuid).get();
    }

    public GroupEntity changeStatus(UUID groupId, GroupStatus status) {
        repository.updateStatus(groupId, status.name());
        return repository.findById(groupId).get();
    }

    public ContractEntity addContractInfo(AddContractInfoRequest request) {
        var contract = ContractEntity.builder()
                        .conclusionDate(strToLocalDate(request.getConclusionDate()))
                        .payerFullname(request.getPayerFullname())
                        .childId(request.getChildId())
                        .paymentAmount(request.getPaymentAmount())
                        .paymentType(request.getPaymentType())
                        .sale(request.getSale())
                        .direction(request.getDirection())
                .build();
        contractRepository.save(contract);
        return null;
    }

    public void addContractFile(MultipartFile file)  {
        try {
            contractRepository.addFile(file.getBytes());
        } catch (Exception e) {
            System.out.println("FILE SAVE ERROR");
        }
    }

    public ResponseEntity<Resource> getContractFile(UUID contractId) {

        var contract = contractRepository.findById(contractId).get();

        return convertContractToResponse(new ByteArrayInputStream(contract.getFile()), "test.jpg");
    }

    private ResponseEntity<Resource> convertContractToResponse(ByteArrayInputStream data, String filename) {
        InputStreamResource file = new InputStreamResource(data);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = " + filename)
                .contentType(MediaType.parseMediaType("image/jpeg"))
                .body(file);
    }

    public JournalRestResponse getJournalForGroup(UUID groupId) {
        var children = childRepository.getAllByGroupId(groupId);
        var classes = classRepository.getClassesInGroup(groupId);
        var attendances = classes
                .stream()
                .flatMap(clas -> attendanceRepository.getAllByClassId(clas.getId()).stream())
                .toList();

        var classesSortByDate = classes
                .stream()
                .sorted(Comparator.comparing(ClassEntity::getClassDateTime))
                .toList();

        var childrenAttendances = children
                .stream()
                .map(child -> {
                    var attendancesByChild = attendances
                            .stream()
                            .filter(attendance -> attendance.getChildId().equals(child.getId()))
                            .toList();

                    List<AttendanceEntity> attendanceEntities = new ArrayList<>();

                    classesSortByDate
                            .forEach(classEntity -> {
                                var attendance = attendancesByChild
                                        .stream()
                                        .filter(attendanceEntity -> attendanceEntity.getClassId().equals(classEntity.getId()))
                                        .findFirst().get();
                                attendanceEntities.add(attendance);
                            });

                    return ChildAttendancesRestResponse.builder()
                            .id(child.getId())
                            .groupId(child.getGroupId())
                            .name(child.getName())
                            .secondName(child.getSecondName())
                            .surname(child.getSurname())
                            .birthDate(child.getBirthDate())
                            .address(child.getAddress())
                            .school(child.getSchool())
                            .classNumber(child.getClassNumber())
                            .email(child.getEmail())
                            .phone(child.getPhone())
                            .parent(child.getParent())
                            .parentPhone(child.getParentPhone())
                            .duplicateKey(child.getDuplicateKey())
                            .status(child.getStatus())
                            .attendances(attendanceRestResponseMapper.mapAll(attendanceEntities))
                            .build();
                })
                .toList();

        return JournalRestResponse
                .builder()
                .classes(classRestResponseMapper.mapAll(classesSortByDate))
                .childrenAttendances(childrenAttendances)
                .build();
    }
}