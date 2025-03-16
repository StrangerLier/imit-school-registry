package omsu.mim.imit.school.registry.rest.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.service.AdminService;
import omsu.mim.imit.school.registry.buiseness.service.ChildService;
import omsu.mim.imit.school.registry.data.entity.enumeration.ChildStatus;
import omsu.mim.imit.school.registry.rest.dto.request.AssistantRequestDto;
import omsu.mim.imit.school.registry.rest.dto.request.FilterChildrenRequestDto;
import omsu.mim.imit.school.registry.rest.dto.request.TeacherRequestDto;
import omsu.mim.imit.school.registry.rest.dto.response.AssistantRestResponse;
import omsu.mim.imit.school.registry.rest.dto.response.ChildRestResponse;
import omsu.mim.imit.school.registry.rest.dto.response.TeacherRestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final ChildService childService;
    private final AdminService adminService;

    @GetMapping("/admin/v1/filter")
    public ResponseEntity<List<ChildRestResponse>> filter(FilterChildrenRequestDto request) {
        request.correctParams();
        return ResponseEntity.ok(childService.filter(request));
    }

    @DeleteMapping("/admin/v1/delete")
    public ResponseEntity<Integer> delete(@RequestParam UUID id) {
        return ResponseEntity.ok(childService.deleteById(id));
    }

    @PostMapping(value = "/admin/v1/changeGroup")
    public ResponseEntity deleteOption(@RequestParam UUID id, @RequestParam UUID groupId) {
        var response = childService.changeGroup(id, groupId);
        return response == null
                ? ResponseEntity.status(505).body("Группа заполнена")
                : ResponseEntity.ok(response);
    }

    @PostMapping("/admin/v1/deactivateChildren")
    public ResponseEntity<ChildRestResponse> activateChildren(@RequestParam UUID id) {
        return ResponseEntity.ok(childService.changeStatus(id, ChildStatus.APPROVED));
    }

    @PostMapping("/admin/v1/changeStatus")
    public ResponseEntity<ChildRestResponse> changeStatus(@RequestParam String id, @RequestParam String status) {
        return ResponseEntity.ok(childService.changeStatus(UUID.fromString(id), ChildStatus.valueOf(status.toUpperCase())));
    }

    @PostMapping("/admin/v1/addTeacher")
    public void createTeacher(@RequestBody TeacherRequestDto request) {
        adminService.registerTeacher(request);
    }

    @PostMapping("/admin/v1/addAssistant")
    public void createAssistant(@RequestBody AssistantRequestDto request) {
        adminService.registerAssistant(request);
    }

    @GetMapping("/admin/v1/teachers")
    public ResponseEntity<List<TeacherRestResponse>> getTeachers() {
        return ResponseEntity.ok(adminService.getTeachers());
    }

    @GetMapping("/admin/v1/assistants")
    public ResponseEntity<List<AssistantRestResponse>> getAssistants() {
        return ResponseEntity.ok(adminService.getAssistants());
    }
}