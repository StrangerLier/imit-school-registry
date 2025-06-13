package omsu.mim.imit.school.registry.rest.controller;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.manager.ApproveChildManager;
import omsu.mim.imit.school.registry.buiseness.service.AdminService;
import omsu.mim.imit.school.registry.buiseness.service.TeacherService;
import omsu.mim.imit.school.registry.rest.dto.request.AssistantRequestDto;
import omsu.mim.imit.school.registry.rest.dto.request.ChildRequestDto;
import omsu.mim.imit.school.registry.rest.dto.request.TeacherRequestDto;
import omsu.mim.imit.school.registry.rest.dto.response.PublicAssistantInfoRestResponse;
import omsu.mim.imit.school.registry.rest.dto.response.PublicTeacherInfoRestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ApproveChildManager approveChildManager;
    private final TeacherService teacherService;
    private final AdminService adminService;

    @PostMapping("/v1/registerChild")
    public void register(@RequestBody ChildRequestDto request) {
        approveChildManager.manage(request);
    }

    @GetMapping("/v1/teachers")
    public List<PublicTeacherInfoRestResponse> getTeachers() {
        return teacherService.getPublicTeachers();
    }

    @GetMapping("/v1/assistants")
    public List<PublicAssistantInfoRestResponse> getAssistants() {
        return teacherService.getPublicAssistants();
    }

    @PostMapping("/v1/addTeacher")
    public void createTeacher(@RequestBody TeacherRequestDto request) {
        adminService.registerTeacher(request);
    }

    @PostMapping("/v1/addAssistant")
    public void createAssistant(@RequestBody AssistantRequestDto request) {
        adminService.registerAssistant(request);
    }
}
