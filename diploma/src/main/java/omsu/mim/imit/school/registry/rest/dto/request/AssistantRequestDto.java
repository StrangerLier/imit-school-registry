package omsu.mim.imit.school.registry.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssistantRequestDto {
    private String name;
    private String secondName;
    private String surname;
    private String phone;
    private String email;
    private int workExperience;
    private String workPlace;
    private Integer rating;
    private List<UUID> desiredTeacherIds;
}
