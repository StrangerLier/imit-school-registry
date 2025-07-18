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
public class AssistantRestResponse {
    private UUID id;
    private String name;
    private String secondName;
    private String surname;
    private String phone;
    private String email;
    private int workExperience;
    private String workPlace;
}
