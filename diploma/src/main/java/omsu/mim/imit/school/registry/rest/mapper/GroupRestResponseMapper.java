package omsu.mim.imit.school.registry.rest.mapper;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import omsu.mim.imit.school.registry.data.entity.GroupEntity;
import omsu.mim.imit.school.registry.rest.dto.response.GroupRestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface GroupRestResponseMapper {

    List<GroupRestResponse> mapAll(List<GroupEntity> entities);

    default GroupRestResponse map(GroupEntity entity) {
        if(entity == null) {
            return null;
        }

        var groupResResp = new GroupRestResponse();

        groupResResp.setId(entity.getId());
        groupResResp.setClassNumber(entity.getClassNumber());
        groupResResp.setDirectionId(entity.getDirectionId());
        groupResResp.setAddress(entity.getAddress());
        groupResResp.setListenersAmount(String.valueOf(entity.getListenersAmount()));
        groupResResp.setApprovedListeners(String.valueOf(entity.getApprovedListeners()));
        groupResResp.setTime(entity.getTime());
        groupResResp.setTeacherId(entity.getTeacherId());
        groupResResp.setDayOfWeek(entity.getDayOfWeek());
        groupResResp.setStatus(entity.getStatus());
        groupResResp.setIsRisky(entity.getIsRisky());

        var assistantsIds = entity.getAssistantsIds();

        if(assistantsIds == null || assistantsIds.isEmpty()) {
            groupResResp.setAssistantsIds(List.of());
        } else {
            var assistantsArr = entity.getAssistantsIds().split(";");
            groupResResp.setAssistantsIds(Arrays.stream(assistantsArr).map(UUID::fromString).toList());
        }

        return groupResResp;
    }
}
