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
import univ.lorraine.simpleChat.SimpleChat.jwtManagement.JwtAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //@Qualifier("userDetailsServiceImpl")

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

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
        http.csrf().disable().authorizeRequests().antMatchers("/registration", "/authentication", "/h2-console/**").permitAll().
                anyRequest().authenticated().and().headers().frameOptions().sameOrigin().and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

}