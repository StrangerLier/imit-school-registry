package omsu.mim.imit.school.registry.buiseness.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.data.repository.ChildRepository;
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
            request.getSchool()
        ));
    }
}
