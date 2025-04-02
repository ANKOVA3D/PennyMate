package PennyMate.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Consenti tutte le richieste su tutte le rotte
                .allowedOrigins("http://localhost")  // Permetti richieste dal tuo frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Permetti metodi HTTP specifici
                .allowedHeaders("*");  // Permetti qualsiasi header
    }
}
