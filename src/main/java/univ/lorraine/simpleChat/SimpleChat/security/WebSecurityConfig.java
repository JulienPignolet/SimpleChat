package univ.lorraine.simpleChat.SimpleChat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import univ.lorraine.simpleChat.SimpleChat.jwtManagement.JwtAuthenticationEntryPoint;
import univ.lorraine.simpleChat.SimpleChat.jwtManagement.JwtRequestFilter;
import univ.lorraine.simpleChat.SimpleChat.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    //@Qualifier("userDetailsServiceImpl")

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    //@Autowired
    //private JwtRequestFilter jwtRequestFilter;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//          http.csrf().disable().anonymous().authorities("ROLE_ANONYMOUS").and()
//                  .authorizeRequests()
//                  //.antMatchers("/resources/**", "/registration","/h2-console/**", "/login", "/registration").permitAll() // Allow to disable authentication security for matching URL .anyRequest().authenticated()
//                  .antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow to disable authentication security for matching URL .anyRequest().authenticated()
//  //                .and().csrf().ignoringAntMatchers("/h2-console/**")//don't apply CSRF protection to /h2-console
//                  .and().headers().frameOptions().sameOrigin().and().//allow use of frame to same origin urls
//                  //.and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().
//                  sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                  .formLogin()
//                  .loginPage("/login").defaultSuccessUrl("/welcome").failureUrl("/login")
//                  .permitAll()
//                  .and().csrf().disable()
//                  .logout()
//                  .permitAll().and().httpBasic().disable();
//
        http.csrf().disable().authorizeRequests().antMatchers("/v2/api-docs",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/registration",
                "/authentication/**",
                "**/**",
                "/h2-console/**",
                "/api/fileUpload/**",
                "/api/groupe/**").permitAll().
                anyRequest().authenticated().and().headers().frameOptions().sameOrigin().and().cors().and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

    //@Override
    //public void addInterceptors(InterceptorRegistry registry){
    //    registry.addInterceptor(new JwtRequestInterceptor()).excludePathPatterns("/swagger-ui.html","/swagger-resources/**","/v2/api-docs","webjars/**","/authentication/**","/registration", "/h2-console/**");
    //}

}
