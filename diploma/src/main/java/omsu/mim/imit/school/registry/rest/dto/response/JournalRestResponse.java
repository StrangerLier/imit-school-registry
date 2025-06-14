package omsu.mim.imit.school.registry.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JournalRestResponse {
    private List<ClassRestResponse> classes;

    private List<ChildAttendancesRestResponse> childrenAttendances;
}
