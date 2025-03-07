package omsu.mim.imit.school.registry.buiseness.manager;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.mapper.ChildMapper;
import omsu.mim.imit.school.registry.buiseness.service.ChildService;
import omsu.mim.imit.school.registry.buiseness.service.GroupService;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import omsu.mim.imit.school.registry.rest.dto.request.ChildRequestDto;
import org.springframework.stereotype.Service;
import java.util.UUID;

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
            request.setGroupId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            groupService.increaseListener(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        } else {
            groupService.increaseListener(group.getId());
        }

        checkForDuplicate(request, group);
    }

    private boolean isNoSlot(GroupEntity group) {
        return group.getApprovedListeners() + 1 > group.getListenersAmount();
    }

    private void checkForDuplicate(ChildRequestDto request, GroupEntity group) {
        var duplicates = childService.findDuplicates(request, group);

        if (duplicates.isEmpty()) {
            return;
        }

        var duplicateKey = getDuplicateKey(request);

        for(ChildEntity duplicate : duplicates) {
            if (StringUtils.isBlank(duplicate.getDuplicateKey())) {
                duplicate.setDuplicateKey(duplicateKey);
                childService.setDuplicateKey(duplicate);
            }
        }

        request.setDuplicateKey(duplicateKey);
    }

    private String getDuplicateKey(ChildRequestDto request) {
        return request.getName() + request.getSurname() + request.getSecondName() + request.getBirthDate();
    }
}