package omsu.mim.imit.school.registry.rest.controller.security;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.service.security.AuthenticationService;
import omsu.mim.imit.school.registry.rest.dto.request.JwtTokenRequest;
import omsu.mim.imit.school.registry.rest.dto.response.JwtTokenResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SecurityController {

    private final AuthenticationService service;

    @PostMapping("/auth")
    public JwtTokenResponse getToken(JwtTokenRequest request) {
        return service.getToken(request);
    }
}
