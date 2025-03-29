package omsu.mim.imit.school.registry.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRequestDto {
    private String name;
    private String secondName;
    private String surname;
    private String phone;
    private String email;
    private int workExperience;
    private String workPlace;
}
