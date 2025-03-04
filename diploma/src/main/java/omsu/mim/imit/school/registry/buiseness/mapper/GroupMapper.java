package omsu.mim.imit.school.registry.buiseness.mapper;

import java.util.UUID;
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
    GroupEntity map(CreateGroupRequest createGroupRequest);

    GroupEntity map(UpdateGroupRequest updateGroupRequest);
}
