package omsu.mim.imit.school.registry.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupRequest {

    private String classNumber;

    private String lecturer;

    private String direction;

    private String address;

    private Integer listenersAmount;

    private String time;
}
