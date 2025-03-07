package omsu.mim.imit.school.registry.rest.dto.response;

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

    private String lecturer;

    private String direction;

    private String address;

    private String listenersAmount;

    private String approvedListeners;

    private String time;
}
