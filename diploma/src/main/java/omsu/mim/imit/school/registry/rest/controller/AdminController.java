package omsu.mim.imit.school.registry.rest.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.service.ChildService;
import omsu.mim.imit.school.registry.data.entity.enumeration.ChildStatus;
import omsu.mim.imit.school.registry.rest.dto.request.FilterChildrenRequestDto;
import omsu.mim.imit.school.registry.rest.dto.response.ChildRestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final ChildService childService;

    @GetMapping("/admin/v1/filter")
    @CrossOrigin(origins = {"https://dip.rkkm.space", "https://dipapi.rkkm.space"})
    public ResponseEntity<List<ChildRestResponse>> filter(FilterChildrenRequestDto request) {
        request.correctParams();
        return ResponseEntity.ok(childService.filter(request));
    }

    @DeleteMapping("/admin/v1/delete")
    @CrossOrigin(origins = {"https://dip.rkkm.space", "https://dipapi.rkkm.space"})
    public ResponseEntity<ChildRestResponse> delete(@RequestParam UUID id) {
        return ResponseEntity.ok(childService.deleteById(id));
    }

    @PostMapping("/admin/v1/deactivateChildren")
    @CrossOrigin(origins = {"https://dip.rkkm.space", "https://dipapi.rkkm.space"})
    public ResponseEntity<ChildRestResponse> activateChildren(@RequestParam UUID id) {
        return ResponseEntity.ok(childService.changeStatus(id, ChildStatus.APPROVED));
    }

    @PostMapping("/admin/v1/changeStatus")
    @CrossOrigin(origins = {"https://dip.rkkm.space", "https://dipapi.rkkm.space"})
    public ResponseEntity<ChildRestResponse> deactivateChildren(@RequestParam UUID id, @RequestParam ChildStatus status) {
        return ResponseEntity.ok(childService.changeStatus(id, status));
    }
}
