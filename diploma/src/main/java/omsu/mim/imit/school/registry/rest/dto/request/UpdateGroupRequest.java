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
public class UpdateGroupRequest {
    private UUID id;

    private String classNumber;

    private UUID teacherId;

    private String direction;

    private String address;

    private Integer listenersAmount;

    private Integer approvedListeners;

    private String time;
}
