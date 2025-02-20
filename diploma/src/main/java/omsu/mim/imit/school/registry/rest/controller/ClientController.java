package omsu.mim.imit.school.registry.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import omsu.mim.imit.school.registry.buiseness.manager.ApproveChildManager;
import omsu.mim.imit.school.registry.buiseness.mapper.ChildMapper;
import omsu.mim.imit.school.registry.buiseness.service.ChildService;
import omsu.mim.imit.school.registry.rest.dto.request.ChildRequestDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ClientController {

    private final ChildService service;
    private final ChildMapper mapper;
    private final ApproveChildManager approveChildManager;

    @PostMapping("/v1/registerChild")
    @CrossOrigin(origins = {"https://dip.rkkm.space", "https://dipapi.rkkm.space"})
    public void register(@RequestBody ChildRequestDto request) {
        approveChildManager.manage(request);
    }

    @GetMapping("/v1/test")
    @CrossOrigin(origins = {"https://dip.rkkm.space", "https://dipapi.rkkm.space"})
    public String test() {
        return "test";
    }
}
