package omsu.mim.imit.school.registry.rest.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.manager.RecommendationManager;
import omsu.mim.imit.school.registry.buiseness.service.AdminService;
import omsu.mim.imit.school.registry.buiseness.service.ChildService;
import omsu.mim.imit.school.registry.data.entity.enumeration.ChildStatus;
import omsu.mim.imit.school.registry.rest.dto.request.DirectionRequestDto;
import omsu.mim.imit.school.registry.rest.dto.request.FilterChildrenRequestDto;
import omsu.mim.imit.school.registry.rest.dto.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final ChildService childService;
    private final AdminService adminService;
    private final RecommendationManager recommendationManager;

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

    @PostMapping("/admin/v1/addDirection")
    public void createDirection(@RequestBody DirectionRequestDto request) {
        adminService.addDirection(request);
    }

    @GetMapping("/admin/v1/teachers")
    public ResponseEntity<List<TeacherRestResponse>> getTeachers() {
        return ResponseEntity.ok(adminService.getTeachers());
    }

    @GetMapping("/admin/v1/assistants")
    public ResponseEntity<List<AssistantRestResponse>> getAssistants() {
        return ResponseEntity.ok(adminService.getAssistants());
    }

    @GetMapping("/admin/v1/directions")
    public ResponseEntity<List<DirectionRestResponse>> getDirections() {
        return ResponseEntity.ok(adminService.getDirections());
    }

    @PostMapping("/admin/addHolidays")
    public void addHolidays(@RequestParam String[] holidays) {
        adminService.addHoliday(holidays);
    }

    @DeleteMapping("/admin/removeHolidays")
    public void removeHolidays(@RequestParam String[] holidaysIds) {
        adminService.removeHolidays(holidaysIds);
    }

    @GetMapping("/admin/recommendations")
    public RecommendationRestResponse removeHolidays() {
        return recommendationManager.createRecommendation();
    }
}