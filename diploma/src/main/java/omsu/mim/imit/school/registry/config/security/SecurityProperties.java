package omsu.mim.imit.school.registry.config.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private Integer jwtExpireInDays;

}

