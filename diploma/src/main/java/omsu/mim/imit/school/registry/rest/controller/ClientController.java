package omsu.mim.imit.school.registry.rest.controller;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.manager.ApproveChildManager;
import omsu.mim.imit.school.registry.buiseness.mapper.ChildMapper;
import omsu.mim.imit.school.registry.buiseness.service.ChildService;
import omsu.mim.imit.school.registry.rest.dto.request.ChildRequestDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ChildService service;
    private final ChildMapper mapper;
    private final ApproveChildManager approveChildManager;

    @PostMapping("/v1/registerChild")
    public void register(@RequestBody ChildRequestDto request) {
        approveChildManager.manage(request);
    }

    @GetMapping("/v1/test")
    public String test() {
        return "test";
    }
}
