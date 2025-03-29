package omsu.mim.imit.school.registry.rest.mapper;

import omsu.mim.imit.school.registry.data.entity.ClassEntity;
import omsu.mim.imit.school.registry.rest.dto.response.ClassRestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ClassRestResponseMapper {
    List<ClassRestResponse> mapAll(List<ClassEntity> entities);

    ClassRestResponse map(ClassEntity entity);
}
