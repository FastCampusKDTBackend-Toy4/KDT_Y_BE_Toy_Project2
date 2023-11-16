package com.kdt_y_be_toy_project2.global.jwt.service;

import com.kdt_y_be_toy_project2.global.jwt.JwtPayload;
import com.kdt_y_be_toy_project2.global.jwt.JwtProvider;
import com.kdt_y_be_toy_project2.global.jwt.api.RefreshTokenRequest;
import com.kdt_y_be_toy_project2.global.jwt.exception.BadTokenException;
import com.kdt_y_be_toy_project2.global.jwt.repository.JwtRepository;
import com.kdt_y_be_toy_project2.global.jwt.repository.JwtEntity;
import com.kdt_y_be_toy_project2.global.jwt.repository.TokenType;
import com.kdt_y_be_toy_project2.global.security.jwt.JwtPair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class JwtService {

    private final JwtRepository jwtRepository;
    private final JwtProvider jwtProvider;

    private final long accessExpiration;
    private final long refreshExpiration;

    public JwtService(JwtRepository jwtRepository, JwtProvider jwtProvider,
        @Value("${service.jwt.access-expiration}") long accessExpiration,
        @Value("${service.jwt.refresh-expiration}") long refreshExpiration) {
        this.jwtRepository = jwtRepository;
        this.jwtProvider = jwtProvider;
        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    public TokenPair createTokenPair(JwtPayload jwtPayload) {
        String accessToken = jwtProvider.createToken(jwtPayload, accessExpiration);
        String refreshToken = jwtProvider.createToken(jwtPayload, refreshExpiration);

        jwtRepository.save(
            new JwtEntity(
                createSaveKey(TokenType.REFRESH, jwtPayload.email()),
                createSaveValue(accessToken, refreshToken),
                refreshExpiration));

        return new TokenPair(accessToken, refreshToken);
    }

    public JwtPayload verifyToken(String token) {
        return jwtProvider.verifyToken(token);
    }

    public JwtPair refreshAccessToken(RefreshTokenRequest request) {
        JwtPayload jwtPayload = verifyToken(request.refreshToken());

        String savedTokenInfo = jwtRepository
            .getByKey(createSaveKey(TokenType.REFRESH, jwtPayload.email()));

        if (!isAcceptable(savedTokenInfo,
            createSaveValue(request.accessToken(), request.refreshToken()))) {
            throw new BadTokenException("적절치 않은 RefreshToken 입니다.");
        }

        String refreshedAccessToken = jwtProvider.createToken(jwtPayload, accessExpiration);

        jwtRepository.save(
            new JwtEntity(
                createSaveKey(TokenType.REFRESH, jwtPayload.email()),
                createSaveValue(refreshedAccessToken, request.refreshToken()),
                refreshExpiration));

        return new JwtPair(refreshedAccessToken, request.refreshToken());
    }

    private boolean isAcceptable(String one, String other) {
        return one.equals(other);
    }

    private String createSaveKey(TokenType tokenType, String memberId) {
        return tokenType.name() + "_" + memberId;
    }

    private String createSaveValue(String accessToken, String refreshToken) {
        return accessToken + ":" + refreshToken;
    }
}
