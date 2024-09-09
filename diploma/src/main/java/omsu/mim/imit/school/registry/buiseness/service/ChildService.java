package omsu.mim.imit.school.registry.buiseness.service;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.data.repository.ChildRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository repository;

    public void save(ChildEntity childEntity) {
        repository.save(childEntity);
    }
}
