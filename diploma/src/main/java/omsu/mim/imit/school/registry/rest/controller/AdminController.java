package omsu.mim.imit.school.registry.rest.controller;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.service.ChildService;
import omsu.mim.imit.school.registry.data.entity.enumeration.ChildStatus;
import omsu.mim.imit.school.registry.rest.dto.request.FilterChildrenRequestDto;
import omsu.mim.imit.school.registry.rest.dto.response.ChildRestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"https://dip.rkkm.space", "https://dipapi.rkkm.space"},
            methods = {RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
@RequiredArgsConstructor
public class AdminController {
    private final ChildService childService;

    @GetMapping("/admin/v1/filter")
    public ResponseEntity<List<ChildRestResponse>> filter(FilterChildrenRequestDto request) {
        request.correctParams();
        return ResponseEntity.ok(childService.filter(request));
    }

    @DeleteMapping("/admin/v1/delete")
    public ResponseEntity<ChildRestResponse> delete(@RequestParam UUID id) {
        return ResponseEntity.ok(childService.deleteById(id));
    }

    @RequestMapping(value = "/admin/v1/delete", method = RequestMethod.OPTIONS)
    public ResponseEntity<Integer> deleteOption() {
        return ResponseEntity.ok(200);
    }
    
    @PostMapping("/admin/v1/deactivateChildren")
    public ResponseEntity<ChildRestResponse> activateChildren(@RequestParam UUID id) {
        return ResponseEntity.ok(childService.changeStatus(id, ChildStatus.APPROVED));
    }

    @PostMapping("/admin/v1/changeStatus")
    public ResponseEntity<ChildRestResponse> deactivateChildren(@RequestParam UUID id, @RequestParam ChildStatus status) {
        return ResponseEntity.ok(childService.changeStatus(id, status));
    }
}
