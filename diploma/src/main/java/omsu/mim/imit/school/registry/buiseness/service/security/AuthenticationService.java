package omsu.mim.imit.school.registry.buiseness.service.security;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import omsu.mim.imit.school.registry.config.security.JwtProvider;
import omsu.mim.imit.school.registry.data.entity.security.ImitUser;
import omsu.mim.imit.school.registry.data.entity.security.Role;
import omsu.mim.imit.school.registry.data.repository.security.RoleRepository;
import omsu.mim.imit.school.registry.data.repository.security.UserRepository;
import omsu.mim.imit.school.registry.rest.dto.request.CreateUserRequest;
import omsu.mim.imit.school.registry.rest.dto.request.JwtTokenRequest;
import omsu.mim.imit.school.registry.rest.dto.response.JwtTokenResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Collection;

@Slf4j
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
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
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

    public void createUserWithRole(CreateUserRequest request) {
        var user = userRepository.save(imitUser(request));
        var role = roleRepository.save(role(request.getRole(), user.getId()));

        log.info("User with login: '{}' and role '{}' has been created", user.getUsername(), role.getRoleName());
    }

    private Collection<? extends GrantedAuthority> getAuthorities(ImitUser imitUser) {
        return roleRepository.findAllByUserId(imitUser.getId()).stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .toList();
    }

    private ImitUser imitUser(CreateUserRequest request) {
        return ImitUser.builder()
            .id(UUID.randomUUID())
            .username(request.getUsername())
            .password(request.getPassword())
            .build();
    }

    private Role role(String roleName, UUID userId) {
        return Role.builder()
            .id(UUID.randomUUID())
            .roleName(roleName)
            .userId(userId)
            .build();
    }
}
