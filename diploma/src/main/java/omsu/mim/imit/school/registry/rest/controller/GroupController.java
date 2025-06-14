package omsu.mim.imit.school.registry.rest.controller;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.mapper.AssistantMapper;
import omsu.mim.imit.school.registry.buiseness.mapper.GroupMapper;
import omsu.mim.imit.school.registry.buiseness.service.GroupService;
import omsu.mim.imit.school.registry.data.entity.enumeration.GroupStatus;
import omsu.mim.imit.school.registry.data.repository.AssistantRepository;
import omsu.mim.imit.school.registry.rest.dto.request.*;
import omsu.mim.imit.school.registry.rest.dto.response.*;
import omsu.mim.imit.school.registry.rest.mapper.*;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService service;
    private final GroupMapper groupMapper;

    private final GroupRestResponseMapper groupRestResponseMapper;
    private final ChildRestResponseMapper childRestResponseMapper;
    private final ClassRestResponseMapper classRestResponseMapper;
    private final AttendanceRestResponseMapper attendanceRestResponseMapper;
    private final ContractRestResponseMapper contractRestResponseMapper;

    private final AssistantRepository assistantRepository;
    private final AssistantMapper assistantMapper;
    private final AssistantRestResponseMapper assistantRestResponseMapper;

    @PostMapping("/group/v1/create")
    public void create(@RequestBody CreateGroupRequest request) {
        service.create(groupMapper.map(request));
    }

    @PostMapping("/group/v1/updateGroup")
    public ResponseEntity<GroupRestResponse> updateGroup(@RequestBody UpdateGroupRequest request) {
        return ResponseEntity.ok(groupRestResponseMapper.map(service.update(groupMapper.map(request))));
    }

    @GetMapping("/group/v1")
    public ResponseEntity<List<GroupRestResponse>> filter() {
        return ResponseEntity.ok(groupRestResponseMapper.mapAll(service.findAll()));
    }

    @GetMapping("/group/v1/downloadAllGroupsInfo")
    public ResponseEntity<Resource> downloadAllGroupsInfo() {
        return service.downloadAllGroupsInfo();
    }

    @GetMapping("/group/v1/downloadChildInGroups")
    public ResponseEntity<Resource> downloadChildInGroups(@RequestParam (value="groupIds") String[] groupIds) {
        var groupIdsList = Arrays.stream(groupIds).map(UUID::fromString).toList();
        return service.downloadChildInGroups(groupIdsList);
    }

    @GetMapping("/group/v1/downloadAllChild")
    public ResponseEntity<Resource> downloadAllChild() {
        return service.downloadAllChild();
    }

    @GetMapping("/group/v1/downloadByDir")
    public ResponseEntity<Resource> downloadAllChild(@RequestParam (value="dirIds") String[] dirIds) {
        var dirIdsList = Arrays.stream(dirIds).map(UUID::fromString).toList();
        return service.downloadChildByDir(dirIdsList);
    }

    @GetMapping("/group/v1/downloadJournal")
    public ResponseEntity<Resource> downloadJournal(@RequestParam (value="groupIds") String[] groupIds) {
        var groupIdsList = Arrays.stream(groupIds).map(UUID::fromString).toList();
        return service.downloadJournalForGroups(groupIdsList);
    }

    @PostMapping("/group/v1/createJournal")
    public void createJournal(@RequestBody CreateScheduleRequest request) throws ParseException {
        service.createJournal(request);
    }

    @GetMapping("/group/v1/getChild")
    public ResponseEntity<List<ChildRestResponse>> getChildInGroup(@RequestParam (value="groupId") UUID groupId) {
        return ResponseEntity.ok(childRestResponseMapper.mapAll(service.getChildList(groupId)));
    }

    @GetMapping("/group/v1/getClasses")
    public ResponseEntity<List<ClassRestResponse>> getClassesInGroup(@RequestParam (value="groupId") UUID groupId) {
        return ResponseEntity.ok(classRestResponseMapper.mapAll(service.getClassesList(groupId)));
    }

    @GetMapping("/group/v1/getAttendance")
    private ResponseEntity<List<AttendanceRestResponse>> getAttendanceInGroup(@RequestParam (value="groupId") UUID groupId) {
        return ResponseEntity.ok(attendanceRestResponseMapper.mapAll(service.getAttendanceInGroup(groupId)));
    }

    @PostMapping("/group/v1/setAttendance")
    private ResponseEntity<AttendanceRestResponse> setAttendance(@RequestBody SetAttendanceRequest request) {
        return ResponseEntity.ok(attendanceRestResponseMapper.map(service.setAttendance(request.getId(), request.getIsAttend(), request.getComment())));
    }

    @PostMapping("/group/v1/setTheme")
    private ResponseEntity<ClassRestResponse> setTheme(@RequestParam (value = "classId") UUID classId,
                                                       @RequestParam (value="theme") String theme) {
        return ResponseEntity.ok(classRestResponseMapper.map(service.setTheme(classId, theme)));
    }

    @PostMapping("/group/v1/addAssistants")
    private ResponseEntity<GroupRestResponse> addAssistant(@RequestParam (value = "groupId") UUID groupId,
                                                            @RequestParam (value = "assistantsIds") String[] assistantsIds) {
        var assistantsIdsList = Arrays.stream(assistantsIds).map(UUID::fromString).toList();
        return ResponseEntity.ok(groupRestResponseMapper.map(service.addAssistants(groupId, assistantsIdsList)));
    }

    @PostMapping("/group/v1/removeAssistants")
    private ResponseEntity<GroupRestResponse> removeAssistants(@RequestParam (value = "groupId") UUID groupId,
                                                           @RequestParam (value = "assistantsIds") String[] assistantsIds) {
        var assistantsIdsList = Arrays.stream(assistantsIds).map(UUID::fromString).toList();
        return ResponseEntity.ok(groupRestResponseMapper.map(service.removeAssistants(groupId, assistantsIdsList)));
    }

    @GetMapping("/group/v1/getAssistants")
    private ResponseEntity<List<AssistantRestResponse>> getAssistants(@RequestParam (value = "groupId") UUID groupId) {
        return ResponseEntity.ok(assistantRestResponseMapper.mapAll(service.getAssistants(groupId)));
    }

    @PostMapping("/group/v1/addContractInfo")
    private ResponseEntity<ContractRestResponse> addContractInfo(@RequestBody AddContractInfoRequest request) {
        return ResponseEntity.ok(contractRestResponseMapper.map(service.addContractInfo(request)));
    }

    @PostMapping("/group/v1/addContractFile")
    private void addContractFile(@RequestParam("contractId") UUID contractId,
                                 @RequestParam("file") MultipartFile file) {
        service.addContractFile(file);
    }

    @GetMapping("/group/v1/contractFile")
    private ResponseEntity<Resource> getContractFile(@RequestParam("contractId") UUID contractId){
        return service.getContractFile(contractId);
    }

    @GetMapping("/group/v1/contractForChild")
    private ResponseEntity<ContractRestResponse> getContractForChild(@RequestParam (value = "childId") UUID childId){
        return ResponseEntity.ok(contractRestResponseMapper.map(service.getContractByChild(childId)));
    }

    @PostMapping("/group/v1/changeClassDate")
    private ResponseEntity<ClassRestResponse> changeClassDate(@RequestParam (value = "classId") UUID classId,
                                                       @RequestParam (value="newDate") String newDate) {
        return ResponseEntity.ok(classRestResponseMapper.map(service.changeClassDate(classId, newDate)));
    }

    @PostMapping("/group/v1/addClass")
    private ResponseEntity<ClassRestResponse> addClass(@RequestParam (value = "groupId") UUID groupId,
                                                              @RequestParam (value="classDate") String classDate) {
        return ResponseEntity.ok(classRestResponseMapper.map(service.addClass(groupId, classDate)));
    }

    @GetMapping("/group/v1/journalForGroup")
    private ResponseEntity<JournalRestResponse> getJournalForGroup(@RequestParam(value = "groupId") UUID groupId) {
        return ResponseEntity.ok(service.getJournalForGroup(groupId));
    }
}