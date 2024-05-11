package co.edu.escuelaing.cvds.ClothCraft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class CookieConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setSameSite("None"); // Establecer SameSite en None
        //serializer.setUseSecureCookie(true); // Asegurar que la cookie solo se envíe a través de HTTPS
        return serializer;
    }
}