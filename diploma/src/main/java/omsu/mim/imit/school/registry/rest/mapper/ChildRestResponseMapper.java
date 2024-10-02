package omsu.mim.imit.school.registry.rest.mapper;

import java.util.List;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.rest.dto.response.ChildRestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ChildRestResponseMapper {

    List<ChildRestResponse> mapAll(List<ChildEntity> entities);

    ChildRestResponse map(ChildEntity entity);
}
