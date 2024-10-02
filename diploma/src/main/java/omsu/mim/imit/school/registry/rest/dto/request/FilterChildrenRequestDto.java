package omsu.mim.imit.school.registry.rest.dto.request;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterChildrenRequestDto {
    private String classNumber;
    private LocalDate birthDate;
    private String surname;
    private String school;
}
