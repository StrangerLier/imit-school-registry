package omsu.mim.imit.school.registry.rest.mapper;

import java.util.List;
import omsu.mim.imit.school.registry.data.entity.ChildEntity;
import omsu.mim.imit.school.registry.data.entity.enumeration.ChildStatus;
import omsu.mim.imit.school.registry.rest.dto.response.ChildRestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ChildRestResponseMapper {

    List<ChildRestResponse> mapAll(List<ChildEntity> entities);

    @Mapping(target = "isActive", source = "status", qualifiedByName = "isStatusActive")
    ChildRestResponse map(ChildEntity entity);

    @Named("isStatusActive")
    static Boolean isStatusActive(ChildStatus status) {
        return ChildStatus.ACTIVE.equals(status);
    }
}
