package omsu.mim.imit.school.registry.rest.mapper;

import omsu.mim.imit.school.registry.data.entity.ContractEntity;
import omsu.mim.imit.school.registry.rest.dto.response.ContractRestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ContractRestResponseMapper {
    List<ContractRestResponse> mapAll(List<ContractEntity> entities);

    ContractRestResponse map(ContractEntity entity);
}
