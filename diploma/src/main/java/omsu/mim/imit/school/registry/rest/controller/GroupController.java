package omsu.mim.imit.school.registry.rest.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.mapper.GroupMapper;
import omsu.mim.imit.school.registry.buiseness.service.GroupService;
import omsu.mim.imit.school.registry.rest.dto.request.CreateGroupRequest;
import omsu.mim.imit.school.registry.rest.dto.response.GroupRestResponse;
import omsu.mim.imit.school.registry.rest.mapper.GroupRestResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService service;
    private final GroupMapper groupMapper;
    private final GroupRestResponseMapper groupRestResponseMapper;

    @PostMapping("/group/v1/create")
    @CrossOrigin(origins = {"https://dip.rkkm.space", "https://dipapi.rkkm.space"})
    public void filter(@RequestBody CreateGroupRequest request) {
        service.create(groupMapper.map(request));
    }

    @GetMapping("/group/v1")
    @CrossOrigin(origins = {"https://dip.rkkm.space", "https://dipapi.rkkm.space"})
    public ResponseEntity<List<GroupRestResponse>> filter() {
        return ResponseEntity.ok(groupRestResponseMapper.mapAll(service.findAll()));
    }
}
