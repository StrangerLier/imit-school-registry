package omsu.mim.imit.school.registry.rest.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/v1/registerChild")
    public void register(@RequestBody ChildRequestDto request) {
        service.save(mapper.map(request));
    }

    @GetMapping("/v1/test")
    public String test() {
        return "test";
    }
}
