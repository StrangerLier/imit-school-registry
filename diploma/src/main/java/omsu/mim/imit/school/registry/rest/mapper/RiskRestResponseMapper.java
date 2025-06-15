package omsu.mim.imit.school.registry.rest.mapper;


import omsu.mim.imit.school.registry.data.entity.RiskEntity;
import omsu.mim.imit.school.registry.rest.dto.response.RiskRestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface RiskRestResponseMapper {
    List<RiskRestResponse> mapAll(List<RiskEntity> entities);

    RiskRestResponse map(RiskEntity entity);
}
