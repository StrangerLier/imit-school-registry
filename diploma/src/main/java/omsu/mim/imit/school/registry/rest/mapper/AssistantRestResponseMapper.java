package omsu.mim.imit.school.registry.rest.mapper;

import omsu.mim.imit.school.registry.data.entity.AssistantEntity;
import omsu.mim.imit.school.registry.rest.dto.response.AssistantRestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AssistantRestResponseMapper {
    List<AssistantRestResponse> mapAll(List<AssistantEntity> entities);

    AssistantRestResponse map(AssistantEntity entity);
}
