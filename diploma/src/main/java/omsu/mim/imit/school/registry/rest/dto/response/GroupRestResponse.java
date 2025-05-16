package omsu.mim.imit.school.registry.rest.dto.response;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRestResponse {
    private UUID id;

    private String classNumber;

    private UUID teacherId;

    private UUID directionId;

    private String address;

    private String listenersAmount;

    private String approvedListeners;

    private String time;

    private String dayOfWeek;

    private List<UUID> assistantsIds;

    private String status;

    private Boolean isRisky;
}
