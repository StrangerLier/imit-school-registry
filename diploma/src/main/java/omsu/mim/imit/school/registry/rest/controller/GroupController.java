package omsu.mim.imit.school.registry.rest.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.mapper.GroupMapper;
import omsu.mim.imit.school.registry.buiseness.service.GroupService;
import omsu.mim.imit.school.registry.rest.dto.request.CreateGroupRequest;
import omsu.mim.imit.school.registry.rest.dto.request.UpdateGroupRequest;
import omsu.mim.imit.school.registry.rest.dto.response.GroupRestResponse;
import omsu.mim.imit.school.registry.rest.mapper.GroupRestResponseMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
public class GroupController {

    private final GroupService service;
    private final GroupMapper groupMapper;
    private final GroupRestResponseMapper groupRestResponseMapper;

    @PostMapping("/group/v1/create")
    public void filter(@RequestBody CreateGroupRequest request) {
        service.create(groupMapper.map(request));
    }

    @PostMapping("/group/v1/updateGroup")
    public void updateGroup(@RequestBody UpdateGroupRequest request) {
        service.update(groupMapper.map(request));
    }

    @GetMapping("/group/v1")
    public ResponseEntity<List<GroupRestResponse>> filter() {
        return ResponseEntity.ok(groupRestResponseMapper.mapAll(service.findAll()));
    }

    @GetMapping("/group/v1/downloadAllGroupsInfo")
    public ResponseEntity<Resource> downloadAllGroupsInfo() {;
        return service.downloadAllGroupsInfo();
    }

    @GetMapping("/group/v1/downloadChildInGroups")
    public ResponseEntity<Resource> downloadChildInGroups(@RequestParam (value="groupIds") String[] groupIds) throws IOException {
        var groupIdsList = Arrays.stream(groupIds).map(UUID::fromString).toList();
        return service.downloadChildInGroups(groupIdsList);
    }

    @GetMapping("/group/v1/downloadAllChild")
    public ResponseEntity<Resource> downloadAllChild() {
        String filename = "child.xlsx";

        return service.downloadAllChild();
    }
}