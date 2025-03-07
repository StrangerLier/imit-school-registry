package omsu.mim.imit.school.registry.buiseness.service.security;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.config.security.JwtProvider;
import omsu.mim.imit.school.registry.data.entity.security.ImitUser;
import omsu.mim.imit.school.registry.data.repository.security.RoleRepository;
import omsu.mim.imit.school.registry.data.repository.security.UserRepository;
import omsu.mim.imit.school.registry.rest.dto.request.JwtTokenRequest;
import omsu.mim.imit.school.registry.rest.dto.response.JwtTokenResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();;


    public JwtTokenResponse getToken(JwtTokenRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow( () ->  new BadCredentialsException("Invalid login or password"));
        var role = roleRepository.findAllByUserId(user.getId()).get(0);
        if (passwordEncoder.encode(passwordEncoder.encode(request.getPassword())).equals(user.getPassword())) {
            return new JwtTokenResponse(jwtProvider.generateToken(user.getUsername()), role.getRoleName());
        } else {
            throw new BadCredentialsException("Invalid login or password");
        }
    }

    public User authenticate(String token) {
        if (token == null || token.isBlank() || !jwtProvider.validateToken(token)) {
            throw new BadCredentialsException("Invalid token");
        }

        String username = jwtProvider.getUsernameFromToken(token);

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        return new User(username, user.getPassword(), getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(ImitUser imitUser) {
        return roleRepository.findAllByUserId(imitUser.getId()).stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .toList();
    }
}
