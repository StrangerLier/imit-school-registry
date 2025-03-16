package omsu.mim.imit.school.registry.rest.mapper;

import java.util.List;
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

    GroupRestResponse map(GroupEntity entity);
}
