package omsu.mim.imit.school.registry.rest.mapper;

import omsu.mim.imit.school.registry.data.entity.AttendanceEntity;
import omsu.mim.imit.school.registry.rest.dto.response.AttendanceRestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AttendanceRestResponseMapper {
    List<AttendanceRestResponse> mapAll(List<AttendanceEntity> entities);

    AttendanceRestResponse map(AttendanceEntity entity);
}
