package omsu.mim.imit.school.registry.buiseness.manager;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.mapper.ChildMapper;
import omsu.mim.imit.school.registry.buiseness.service.ChildService;
import omsu.mim.imit.school.registry.buiseness.service.GroupService;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import omsu.mim.imit.school.registry.rest.dto.request.ChildRequestDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApproveChildManager {

    private final GroupService groupService;
    private final ChildService childService;
    private final ChildMapper childMapper;

    public void manage(ChildRequestDto request) {
        var group = groupService.findById(request.getGroupId());
        validateRequest(request, group);
        childService.save(childMapper.map(request));
    }

    private void validateRequest(ChildRequestDto request, GroupEntity group) {
        if (isNoSlot(group)) {
            throw new RuntimeException("Группа заполнена.");
        }

        if (isDuplicate(request)) {
            throw new RuntimeException("Ребенок '%s' '%s' '%s', уже записан на направление: '%s'".formatted(
                request.getName(),
                request.getSurname(),
                request.getSecondName(),
                group.getDirection()
            ));
        }

    }

    private boolean isNoSlot(GroupEntity group) {
        return group.getApprovedListeners() + 1 > group.getListenersAmount();
    }

    private boolean isDuplicate(ChildRequestDto request) {
        var child = childService.findDuplicate(request);

        return child.isPresent();
    }
}
