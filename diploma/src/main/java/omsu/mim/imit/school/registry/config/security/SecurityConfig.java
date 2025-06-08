package omsu.mim.imit.school.registry.config.security;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.util.constant.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors().and()
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                    auth -> {
                        auth.requestMatchers("/admin/**").hasRole(SecurityConstants.ADMIN_ROLE)
                            .requestMatchers("/group/**").hasRole(SecurityConstants.ADMIN_ROLE)
                            .requestMatchers("/group/v1/setAttendance").hasAnyRole(SecurityConstants.ASSISTANT_ROLE, SecurityConstants.TEACHER_ROLE)
                            .requestMatchers("/group/v1/setTheme").hasAnyRole(SecurityConstants.ASSISTANT_ROLE, SecurityConstants.TEACHER_ROLE)
                            .requestMatchers("/group/v1/setAssistantAttendance").hasRole(SecurityConstants.ASSISTANT_ROLE)
                            .requestMatchers("/parent/v1/children").hasRole(SecurityConstants.PARENT_ROLE)
                            .requestMatchers("/auth/**").permitAll()
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .anyRequest().permitAll();
                    }
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .permitAll())
                .build();
    }
}