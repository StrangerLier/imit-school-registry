package omsu.mim.imit.school.registry.buiseness.service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import omsu.mim.imit.school.registry.data.repository.ChildRepository;
import omsu.mim.imit.school.registry.data.repository.GroupRepository;
import omsu.mim.imit.school.registry.rest.mapper.GroupExcelMapper;
import omsu.mim.imit.school.registry.util.exception.ObjectNotFoundException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;
    private final ChildRepository childRepository;

    public void create(GroupEntity entity) {
        repository.save(entity);
    }

    public void update(GroupEntity entity) {
        repository.update(entity);
    }

    public ResponseEntity<Resource> downloadAllGroupsInfo() {
        List<GroupEntity> groupEntities = repository.findAll();
        return convertToResponse(GroupExcelMapper.allGroupsToExcel(groupEntities),
                "groups.xlsx");
    }

    public ResponseEntity<Resource> downloadChildInGroups(List<UUID> groupIds) {
        List<GroupEntity> groupEntities = repository.findAllById(groupIds);
        List<ChildEntity> childEntities = repository.getChildInGroups(groupIds);

        var groupsSet = groupEntities.stream().collect(Collectors.toUnmodifiableSet());

        return convertToResponse(GroupExcelMapper.childInGroupsToExcel(childEntities, groupsSet),
                "child_in_group.xlsx");
    }

    public ResponseEntity<Resource> downloadAllChild() {
        List<ChildEntity> childEntities = childRepository.findAll();
        List<GroupEntity> groupEntities = repository.findAll();

        var groupsByIdMap = groupEntities.stream().collect(Collectors.toMap(GroupEntity::getId, Function.identity()));

        return convertToResponse(GroupExcelMapper.allChildToExcel(childEntities, groupsByIdMap),
                "child.xlsx");
    }

    public List<GroupEntity> findAll() {
        return repository.findAll();
    }

    public GroupEntity findById(UUID groupId) {
        return repository.findById(groupId)
                .orElseThrow(() -> new ObjectNotFoundException("Group with id '%s' is not found".formatted(groupId)));
    }

    public void increaseListener(UUID groupId) {
        repository.increaseListenersAmount(groupId);
    }

    private ResponseEntity<Resource> convertToResponse(ByteArrayInputStream data, String filename) {
        InputStreamResource file = new InputStreamResource(data);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = " + filename)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }

}