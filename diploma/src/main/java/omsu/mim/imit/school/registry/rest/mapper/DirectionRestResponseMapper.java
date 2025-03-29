package omsu.mim.imit.school.registry.rest.mapper;

import java.util.List;
import omsu.mim.imit.school.registry.data.entity.DirectionEntity;
import omsu.mim.imit.school.registry.rest.dto.response.DirectionRestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface DirectionRestResponseMapper {

    List<DirectionRestResponse> mapAll(List<DirectionEntity> entities);

    DirectionRestResponse map(DirectionEntity entity);
}
