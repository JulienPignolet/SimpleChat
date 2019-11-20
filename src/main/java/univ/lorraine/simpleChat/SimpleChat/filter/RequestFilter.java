package univ.lorraine.simpleChat.SimpleChat.filter;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) httpServletRequest;
        final String val = request.getMethod() + " " + request.getRequestURI();

        if(val == null || !val.equals("")) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is not valid.");
        }
        else
        {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }

    }
}
