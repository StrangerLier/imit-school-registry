package omsu.mim.imit.school.registry.config;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.service.security.CustomUserDetailsService;
import omsu.mim.imit.school.registry.util.constant.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(
                    auth -> {
                        auth.requestMatchers("/admin/**").hasRole(SecurityConstants.ADMIN_ROLE)
                            .requestMatchers("/group/**").hasRole(SecurityConstants.ADMIN_ROLE)
                            .anyRequest().permitAll();
                    }
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(login -> login
                    .loginPage("/auth")
                    .permitAll()
                )
                .logout(logout -> logout
                    .logoutUrl("/logout")
                    .permitAll())
                .build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        return authManagerBuilder.build();
    }
}