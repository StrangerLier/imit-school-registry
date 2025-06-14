package omsu.mim.imit.school.registry.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import omsu.mim.imit.school.registry.data.entity.enumeration.ChildStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildAttendancesRestResponse {

    private UUID id;
    private UUID groupId;

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

    private String parent;

    private String parentPhone;

    private String duplicateKey;

    private ChildStatus status;

    private List<AttendanceRestResponse> attendances;
}
