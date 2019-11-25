package univ.lorraine.simpleChat.SimpleChat.jwtManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.MappedInterceptor;

@Configuration
public class InterceptorsConfig implements WebMvcConfigurer{

    @Autowired
    private JwtRequestInterceptor jwtRequestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new JwtRequestInterceptor()).excludePathPatterns("/swagger-ui.html","/swagger-resources/**","/v2/api-docs","webjars/**","/authentication","/registration", "/h2-console/**").pathMatcher(new AntPathMatcher());
    }
    @Bean
    public MappedInterceptor myInterceptor()
    {
        String [] excludePatterns = new String[] {"/swagger-ui.html","/swagger-resources/**","/v2/api-docs","webjars/**","/authentication/**","/registration", "/h2-console/**"};
        return new MappedInterceptor(null, excludePatterns, new JwtRequestInterceptor());
    }
}
