package omsu.mim.imit.school.registry.rest.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.service.ChildService;
import omsu.mim.imit.school.registry.rest.dto.response.ChildRestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParentController {

    private final ChildService childService;

    @GetMapping("/parent/v1/children")
    public List<ChildRestResponse> getChildren(@RequestParam List<UUID> childrenId) {
        return childService.findAll(childrenId);
    }
}
