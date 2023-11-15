package com.kdt_y_be_toy_project2.global.jwt.api;

import com.kdt_y_be_toy_project2.global.jwt.service.JwtService;
import com.kdt_y_be_toy_project2.global.security.jwt.JwtPair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtRefreshController {

    private final JwtService jwtService;

    public JwtRefreshController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/v1/refresh")
    public ResponseEntity<JwtPair> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(jwtService.refreshAccessToken(request));
    }
}
