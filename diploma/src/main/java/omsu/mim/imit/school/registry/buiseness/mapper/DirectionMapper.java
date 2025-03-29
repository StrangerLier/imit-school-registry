package omsu.mim.imit.school.registry.buiseness.mapper;

import omsu.mim.imit.school.registry.data.entity.DirectionEntity;
import omsu.mim.imit.school.registry.rest.dto.request.DirectionRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        imports = UUID.class
)
public interface DirectionMapper {
    @Mapping(target = "id", expression = ("java(UUID.randomUUID())"))
    DirectionEntity map(DirectionRequestDto dto);
}
