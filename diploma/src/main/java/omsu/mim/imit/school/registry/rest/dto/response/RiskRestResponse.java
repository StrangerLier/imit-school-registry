package omsu.mim.imit.school.registry.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskRestResponse {
    private UUID id;

    private UUID groupId;

    private UUID supportId;

    private String comment;

    private String reason;
}
