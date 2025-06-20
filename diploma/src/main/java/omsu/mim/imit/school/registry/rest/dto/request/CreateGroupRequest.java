package omsu.mim.imit.school.registry.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupRequest {
    private String classNumber;

    private UUID teacherId;

    private UUID directionId;

    private String address;

    private Integer listenersAmount;

    private String time;

    private String dayOfWeek;
}
