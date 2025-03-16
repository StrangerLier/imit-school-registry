package omsu.mim.imit.school.registry.buiseness.mapper;

import omsu.mim.imit.school.registry.data.entity.AssistantEntity;
import omsu.mim.imit.school.registry.rest.dto.request.AssistantRequestDto;
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
public interface AssistantMapper {
    @Mapping(target = "id", expression = ("java(UUID.randomUUID())"))
    AssistantEntity map(AssistantRequestDto dto);
}
