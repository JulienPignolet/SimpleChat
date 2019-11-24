package univ.lorraine.simpleChat.SimpleChat.jwtManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorsConfig implements WebMvcConfigurer {

    @Autowired
    private JwtRequestInterceptor jwtRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(this.jwtRequestInterceptor).excludePathPatterns("/swagger-ui.html","/swagger-resources/**","/v2/api-docs","webjars/**","/authentication","/registration", "/h2-console/**");
    }
}
