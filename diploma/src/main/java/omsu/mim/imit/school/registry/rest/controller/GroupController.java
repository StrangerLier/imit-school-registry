package omsu.mim.imit.school.registry.rest.controller;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.mapper.GroupMapper;
import omsu.mim.imit.school.registry.buiseness.service.GroupService;
import omsu.mim.imit.school.registry.rest.dto.request.CreateGroupRequest;
import omsu.mim.imit.school.registry.rest.dto.request.CreateScheduleRequest;
import omsu.mim.imit.school.registry.rest.dto.request.UpdateGroupRequest;
import omsu.mim.imit.school.registry.rest.dto.response.AttendanceRestResponse;
import omsu.mim.imit.school.registry.rest.dto.response.ChildRestResponse;
import omsu.mim.imit.school.registry.rest.dto.response.ClassRestResponse;
import omsu.mim.imit.school.registry.rest.dto.response.GroupRestResponse;
import omsu.mim.imit.school.registry.rest.mapper.AttendanceRestResponseMapper;
import omsu.mim.imit.school.registry.rest.mapper.ChildRestResponseMapper;
import omsu.mim.imit.school.registry.rest.mapper.ClassRestResponseMapper;
import omsu.mim.imit.school.registry.rest.mapper.GroupRestResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService service;
    private final GroupMapper groupMapper;

    private final GroupRestResponseMapper groupRestResponseMapper;
    private final ChildRestResponseMapper childRestResponseMapper;
    private final ClassRestResponseMapper classRestResponseMapper;
    private final AttendanceRestResponseMapper attendanceRestResponseMapper;

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
    public ResponseEntity<Resource> downloadAllChild(@RequestParam (value="dirList") String[] dirList) {
        return service.downloadChildByDir(dirList);
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
    private ResponseEntity<AttendanceRestResponse> setAttendance(@RequestParam (value = "attendanceId") UUID attendanceId,
                                                                 @RequestParam (value="isAttend") Boolean isAttend,
                                                                 @RequestParam (value = "comment") String comment) {
        return ResponseEntity.ok(attendanceRestResponseMapper.map(service.setAttendance(attendanceId, isAttend, comment)));
    }

    @PostMapping("/group/v1/setTheme")
    private ResponseEntity<ClassRestResponse> setTheme(@RequestParam (value = "classId") UUID classId,
                                                       @RequestParam (value="theme") String theme) {
        return ResponseEntity.ok(classRestResponseMapper.map(service.setTheme(classId, theme)));
    }
}