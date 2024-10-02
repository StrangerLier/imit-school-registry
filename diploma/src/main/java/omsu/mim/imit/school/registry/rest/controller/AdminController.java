package omsu.mim.imit.school.registry.rest.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.service.ChildService;
import omsu.mim.imit.school.registry.rest.dto.request.FilterChildrenRequestDto;
import omsu.mim.imit.school.registry.rest.dto.response.ChildRestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final ChildService childService;

    @GetMapping("/admin/v1/filter")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<ChildRestResponse>> filter(FilterChildrenRequestDto request) {
        return ResponseEntity.ok(childService.filter(request));
    }
}
