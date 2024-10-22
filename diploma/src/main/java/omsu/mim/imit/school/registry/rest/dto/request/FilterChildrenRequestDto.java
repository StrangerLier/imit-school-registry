package omsu.mim.imit.school.registry.rest.dto.request;

import io.micrometer.common.util.StringUtils;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterChildrenRequestDto {

    private String classNumber;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;
    private String surname;
    private String school;

    public void correctParams() {
        if (StringUtils.isEmpty(classNumber)) {
            classNumber = null;
        }

        if (StringUtils.isEmpty(surname)) {
            surname = null;
        }

        if (StringUtils.isEmpty(school)) {
            school = null;
        }
    }
}
