package univ.lorraine.simpleChat.SimpleChat.jwtManagement;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import univ.lorraine.simpleChat.SimpleChat.service.UserDetailsServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl jwtUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = httpServletRequest.getHeader("user_key");
        String username = null;

        if (requestTokenHeader != null){
            try{
                username = jwtTokenUtil.getUsernameFromToken(requestTokenHeader);
            } catch (IllegalArgumentException e){
                System.out.println("Impossible de récupérer le JWT");
            } catch (ExpiredJwtException e) {
                System.out.println("Le JWT a expiré");
            }

        }
        else {
            logger.warn("Le JWT n'existe pas");
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(requestTokenHeader, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        //HttpServletRequest request = (HttpServletRequest) httpServletRequest;
        //final String val = request.getMethod() + " " + request.getRequestURI();

        //if(val == null || !val.equals("")) {
        //    httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
        //}
        //else
        //{
        //    filterChain.doFilter(httpServletRequest, httpServletResponse);
        //}

    }
}
