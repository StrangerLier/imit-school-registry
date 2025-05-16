package omsu.mim.imit.school.registry.buiseness.mapper;

import java.util.UUID;
import java.util.stream.Collectors;

import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import omsu.mim.imit.school.registry.rest.dto.request.CreateGroupRequest;
import omsu.mim.imit.school.registry.rest.dto.request.UpdateGroupRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    imports = UUID.class
)
public interface GroupMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "approvedListeners", constant = "0")
    @Mapping(target = "assistantsIds", ignore = true)
    @Mapping(target = "isRisky", ignore = true)
    @Mapping(target = "status", constant = "NEW")
    GroupEntity map(CreateGroupRequest createGroupRequest);

    default GroupEntity map(UpdateGroupRequest updateGroupRequest) {
        
        var groupEntity = GroupEntity.builder().build();

        groupEntity.setId(updateGroupRequest.getId());
        groupEntity.setClassNumber(updateGroupRequest.getClassNumber());
        groupEntity.setDirectionId(updateGroupRequest.getDirectionId());
        groupEntity.setAddress(updateGroupRequest.getAddress());
        groupEntity.setListenersAmount(updateGroupRequest.getListenersAmount());
        groupEntity.setApprovedListeners(updateGroupRequest.getApprovedListeners());
        groupEntity.setTime(updateGroupRequest.getTime());
        groupEntity.setTeacherId(updateGroupRequest.getTeacherId());
        groupEntity.setDayOfWeek(updateGroupRequest.getDayOfWeek());

        var assistantsIds = updateGroupRequest.getAssistantsIds();

        if(assistantsIds == null || assistantsIds.isEmpty()) {
            groupEntity.setAssistantsIds(null);
        } else {
            groupEntity.setAssistantsIds(
                    assistantsIds
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(";")));
        }
        return groupEntity;
    }
    
}
