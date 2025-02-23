package omsu.mim.imit.school.registry.buiseness.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.data.entity.enumeration.ChildStatus;
import omsu.mim.imit.school.registry.data.repository.ChildRepository;
import omsu.mim.imit.school.registry.rest.dto.request.ChildRequestDto;
import omsu.mim.imit.school.registry.rest.dto.request.FilterChildrenRequestDto;
import omsu.mim.imit.school.registry.rest.dto.response.ChildRestResponse;
import omsu.mim.imit.school.registry.rest.mapper.ChildRestResponseMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRestResponseMapper childRestResponseMapper;
    private final ChildRepository repository;

    public void save(ChildEntity childEntity) {
        repository.save(childEntity);
    }

    public List<ChildRestResponse> filter(FilterChildrenRequestDto request) {
        return childRestResponseMapper.mapAll(repository.filter(
            request.getClassNumber(),
            request.getBirthDate(),
            request.getSurname(),
            request.getSchool(),
            request.getGroupId()
        ));
    }

    public ChildRestResponse deleteById(UUID id) {
        var child = repository.findById(id).get();

        child.setStatus(ChildStatus.ARCHIVED);
        repository.save(child);

        return childRestResponseMapper.map(child);
    }

    public ChildRestResponse changeStatus(UUID id, ChildStatus status) {
        var child = repository.findById(id).get();

        child.setStatus(status);
        repository.save(child);

        return childRestResponseMapper.map(child);
    }

    public List<ChildEntity> findDuplicates(ChildRequestDto request) {
        return repository.findForDuble(
                request.getName(),
                request.getSecondName(),
                request.getSurname(),
                request.getBirthDate(),
                request.getGroupId()
            );
    }

    public void setDuplicateKey(ChildEntity childEntity) {
        repository.save(childEntity);
    }
}
