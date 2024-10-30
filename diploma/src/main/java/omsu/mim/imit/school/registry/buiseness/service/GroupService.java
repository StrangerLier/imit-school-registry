package omsu.mim.imit.school.registry.buiseness.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import omsu.mim.imit.school.registry.data.repository.GroupRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository repository;

    public void create(GroupEntity entity) {
        repository.save(entity);
    }

    public List<GroupEntity> findAll() {
        return repository.findAll();
    }
}
