package com.kdt_y_be_toy_project2.global.security.formlogin;

import com.kdt_y_be_toy_project2.global.config.CustomHttpHeaders;
import com.kdt_y_be_toy_project2.global.jwt.JwtPayload;
import com.kdt_y_be_toy_project2.global.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class CustomFormLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtProvider jwtProvider;

    public CustomFormLoginFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        super("/v1/login");
        setAuthenticationManager(authenticationManager);
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        return getAuthenticationManager().authenticate(
            CustomLoginToken.unAuthenticate(username, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String email = (String) authResult.getPrincipal();

        response.setHeader(
            CustomHttpHeaders.ACCESS_TOKEN, jwtProvider.createAccessToken(new JwtPayload(email, new Date())));
        response.setHeader(CustomHttpHeaders.REFRESH_TOKEN, jwtProvider.createRefreshToken(new JwtPayload(email, new Date())));
    }
}
