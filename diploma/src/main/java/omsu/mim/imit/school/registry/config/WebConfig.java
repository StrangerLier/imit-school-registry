package omsu.mim.imit.school.registry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") //or whichever methods you want to allow
                .allowedOrigins("*") //or www.example.com if you want to be more specific
                .allowedHeaders("Content_Type", "Authorization"); //i also put Authorization since i saw you probably want to do so
    }
}