package omsu.mim.imit.school.registry.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class PublicAssistantInfoRestResponse {
    private UUID id;
    private String initial;
}
