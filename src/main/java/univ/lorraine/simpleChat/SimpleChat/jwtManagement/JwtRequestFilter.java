package univ.lorraine.simpleChat.SimpleChat.jwtManagement;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import univ.lorraine.simpleChat.SimpleChat.service.UserDetailsServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        final String requestTokenHeader = httpServletRequest.getHeader("user_key");
        String username = null;
        jwtTokenUtil = new JwtTokenUtil();

        if (requestTokenHeader != null){
            try{
                username = jwtTokenUtil.getUsernameFromToken(requestTokenHeader);
            } catch (IllegalArgumentException e){
                System.out.println("Impossible de récupérer le JWT" + e.getMessage());
            } catch (ExpiredJwtException e) {
                System.out.println("Le JWT a expiré");
            }catch (Exception e){
                //System.out.println("JWT absent ou mal formé");
            }
        }
        else {
            System.out.println("Le JWT n'existe pas");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //if (username != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(requestTokenHeader, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
         Collection<String> excludeUrlPatterns = new ArrayList<>();
         excludeUrlPatterns.add("/authentication/**");
        excludeUrlPatterns.add("/authentication");
        excludeUrlPatterns.add("/swagger-resources/**");
         excludeUrlPatterns.add("/configuration/ui");
         excludeUrlPatterns.add("/configuration/security");
         excludeUrlPatterns.add("/swagger-ui.html");
         excludeUrlPatterns.add("/webjars/**");
         excludeUrlPatterns.add("/registration");
         excludeUrlPatterns.add("/v2/api-docs");
         excludeUrlPatterns.add("/h2-console/**");
        return excludeUrlPatterns.stream()
                .anyMatch(p -> antPathMatcher.match(p, request.getRequestURI())); }
}
