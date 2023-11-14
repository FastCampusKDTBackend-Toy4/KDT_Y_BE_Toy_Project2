package com.kdt_y_be_toy_project2.global.security.jwt;

import com.kdt_y_be_toy_project2.global.config.CustomHttpHeaders;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws ServletException, IOException {
        if (!hasJwtToken(request)) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = request.getHeader(CustomHttpHeaders.ACCESS_TOKEN);
        String refreshToken = request.getHeader(CustomHttpHeaders.REFRESH_TOKEN);

        SecurityContextHolder.getContext()
            .setAuthentication(authenticationManager.authenticate(
                JwtAuthenticationToken.unAuthorizeToken(accessToken, refreshToken)));

        chain.doFilter(request, response);
    }

    private boolean hasJwtToken(HttpServletRequest request) {
        return request.getHeader(CustomHttpHeaders.ACCESS_TOKEN) != null &&
            request.getHeader(CustomHttpHeaders.REFRESH_TOKEN) != null;
    }
}
