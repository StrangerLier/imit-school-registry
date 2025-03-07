package omsu.mim.imit.school.registry.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtTokenRequest {
    private String username;
    private String password;
}
