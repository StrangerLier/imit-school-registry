package omsu.mim.imit.school.registry.rest.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateParentRequestDto {
    private String username;
    private String password;
    private UUID childId;
}
