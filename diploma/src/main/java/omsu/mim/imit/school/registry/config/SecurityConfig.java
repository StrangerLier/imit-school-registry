package omsu.mim.imit.school.registry.config;

import lombok.RequiredArgsConstructor;
import omsu.mim.imit.school.registry.buiseness.service.security.CustomUserDetailsService;
import omsu.mim.imit.school.registry.util.constant.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                    auth -> {
                        auth.requestMatchers("/admin/**").hasRole(SecurityConstants.ADMIN_ROLE)
                            .requestMatchers("/group/**").hasRole(SecurityConstants.ADMIN_ROLE)
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            .anyRequest().permitAll();
                    }
                )
                .httpBasic(Customizer.withDefaults())
//                .formLogin(login -> login
//                    .loginPage("https://dip.rkkm.space/auth")
//                    .permitAll()
//                )
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