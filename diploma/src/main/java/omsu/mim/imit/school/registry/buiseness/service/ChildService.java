package omsu.mim.imit.school.registry.buiseness.service;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import omsu.mim.imit.school.registry.data.entity.enumeration.ChildStatus;
import omsu.mim.imit.school.registry.data.repository.ChildRepository;
import omsu.mim.imit.school.registry.data.repository.GroupRepository;
import omsu.mim.imit.school.registry.rest.dto.request.ChildRequestDto;
import omsu.mim.imit.school.registry.rest.dto.request.FilterChildrenRequestDto;
import omsu.mim.imit.school.registry.rest.dto.response.ChildRestResponse;
import omsu.mim.imit.school.registry.rest.mapper.ChildRestResponseMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRestResponseMapper childRestResponseMapper;
    private final ChildRepository childRepository;
    private final GroupRepository groupRepository;

    public void save(ChildEntity childEntity) {
        childRepository.save(childEntity);
    }

    public List<ChildRestResponse> filter(FilterChildrenRequestDto request) {

        var filtered = childRestResponseMapper.mapAll(childRepository.filter(
                request.getClassNumber(),
                request.getBirthDate(),
                request.getSurname(),
                request.getSchool(),
                request.getGroupId()
        ));

        var filteredGroups = groupRepository.findAllById(
                        filtered.stream()
                            .map(ChildRestResponse::getGroupId)
                            .toList()
                        );

        var filteredGroupsMap = filteredGroups.stream()
                    .collect(Collectors.toMap(GroupEntity::getId, Function.identity()));

        return filtered.stream()
                .filter(v -> !StringUtils.isBlank(v.getDuplicateKey()))
                .flatMap(response -> {
                    var group = filteredGroupsMap.get(response.getGroupId());
                    var duplicates = childRepository.findForDuble(
                            response.getName(),
                            response.getSecondName(),
                            response.getSurname(),
                            response.getBirthDate(),
                            group.getDirection(),
                            group.getTime()
                    );
                    return childRestResponseMapper.mapAll(duplicates).stream();
                })
                .distinct()
                .toList();
    }

    public ChildRestResponse deleteById(UUID id) {
        var child = childRepository.findById(id).get();

        child.setStatus(ChildStatus.ARCHIVED);
        childRepository.save(child);

        return childRestResponseMapper.map(child);
    }

    public ChildRestResponse changeStatus(UUID id, ChildStatus status) {
        var child = childRepository.findById(id).get();

        child.setStatus(status);
        childRepository.save(child);

        return childRestResponseMapper.map(child);
    }

    public List<ChildEntity> findDuplicates(ChildRequestDto request, GroupEntity group) {
        return childRepository.findForDuble(
                request.getName(),
                request.getSecondName(),
                request.getSurname(),
                request.getBirthDate(),
                group.getDirection(),
                group.getTime()
        );
    }

    public void setDuplicateKey(ChildEntity childEntity) {
        childRepository.save(childEntity);
    }
}