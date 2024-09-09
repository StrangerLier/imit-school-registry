package omsu.mim.imit.school.registry.buiseness.mapper;

import java.util.UUID;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.rest.dto.request.ChildRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    imports = UUID.class
)
public interface ChildMapper {

    @Mapping(target = "id", expression = ("java(UUID.randomUUID())"))
    ChildEntity map(ChildRequestDto dto);
}
