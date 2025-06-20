package omsu.mim.imit.school.registry.rest.dto.request;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildRequestDto {

    private String name;

    private String secondName;

    private String surname;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    private String address;

    private String school;

    private String classNumber;

    private String email;

    private String phone;

    private UUID groupId;

    private String parentPhone;

    private String parent;

    private String skills;

    private String duplicateKey;
}
