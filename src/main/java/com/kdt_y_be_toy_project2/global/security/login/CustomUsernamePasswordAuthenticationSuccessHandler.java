package com.kdt_y_be_toy_project2.global.security.login;

import com.kdt_y_be_toy_project2.global.config.CustomHttpHeaders;
import com.kdt_y_be_toy_project2.global.jwt.JwtPayload;
import com.kdt_y_be_toy_project2.global.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomUsernamePasswordAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;

    public CustomUsernamePasswordAuthenticationSuccessHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        AccountContext accountContext = (AccountContext) authentication.getPrincipal();
        String email = accountContext.getUsername();
        String accessToken = jwtProvider.createAccessToken(new JwtPayload(email, new Date()));
        String refreshToken = jwtProvider.createRefreshToken(new JwtPayload(email, new Date()));

        response.setHeader(CustomHttpHeaders.ACCESS_TOKEN, accessToken);
        response.setHeader(CustomHttpHeaders.REFRESH_TOKEN, refreshToken);
    }
}
