package co.edu.escuelaing.cvds.ClothCraft.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000","https://mango-cliff-06b900910.5.azurestaticapps.net")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

}
