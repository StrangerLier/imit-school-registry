package omsu.mim.imit.school.registry.buiseness.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import omsu.mim.imit.school.registry.data.repository.GroupRepository;
import omsu.mim.imit.school.registry.rest.mapper.GroupExcelMapper;
import omsu.mim.imit.school.registry.util.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;

    public void create(GroupEntity entity) {
        repository.save(entity);
    }

    public void update(GroupEntity entity) {
        repository.update(entity);
    }

    public ByteArrayInputStream download() throws IOException {
        List<GroupEntity> groupEntities = repository.findAll();
        return  GroupExcelMapper.dataToExcel(groupEntities);
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
}
