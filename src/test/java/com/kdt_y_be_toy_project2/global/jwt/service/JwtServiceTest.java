package com.kdt_y_be_toy_project2.global.jwt.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kdt_y_be_toy_project2.global.jwt.JwtPayload;
import com.kdt_y_be_toy_project2.global.jwt.api.RefreshTokenRequest;
import com.kdt_y_be_toy_project2.global.jwt.exception.BadTokenException;
import com.kdt_y_be_toy_project2.global.jwt.exception.TokenExpiredException;
import com.kdt_y_be_toy_project2.global.jwt.repository.JwtRepository;
import com.kdt_y_be_toy_project2.global.security.jwt.JwtPair;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtRepository jwtRepository;

    private final SecretKey secretKey;

    public JwtServiceTest(@Value("${service.jwt.secret-key}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }

    @DisplayName("토큰 발급은")
    @Nested
    class Context_createTokenPair {

        @DisplayName("토큰을 발급하고, key = REFRESH_email, value = accessToken:refreshToken 으로 저장한다.")
        @Test
        void _willSuccess() {

            // given
            Date date = new Date();
            JwtPayload jwtPayload = new JwtPayload("test@email.com", date);
            String savedKey = "REFRESH_" + jwtPayload.email();

            // when
            TokenPair tokenPair = jwtService.createTokenPair(jwtPayload);

            // then
            JwtParser parser = Jwts.parser().verifyWith(secretKey).build();

            assertDoesNotThrow(() -> parser.parseSignedClaims(tokenPair.accessToken()));
            assertDoesNotThrow(() -> parser.parseSignedClaims(tokenPair.refreshToken()));

            String expectSavedTokenValue = tokenPair.accessToken() + ":" + tokenPair.refreshToken();
            String actualSavedTokenValue = jwtRepository.getByKey(savedKey);

            Assertions.assertEquals(expectSavedTokenValue, actualSavedTokenValue);
        }

        @DisplayName("email이 null이면 실패한다.")
        @Test
        void emailNull_willFail() {

            // given
            Date date = new Date();
            JwtPayload jwtPayload = new JwtPayload(null, date);

            // when then
            assertThrows(NullPointerException.class, () -> jwtService.createTokenPair(jwtPayload));
        }


        @DisplayName("발급 일자(issuedAt)가 null 이면 발급에 실패한다.")
        @Test
        void issuedAtNull_willFail() {

            // given
            JwtPayload jwtPayload = new JwtPayload("test@email.com", null);

            // when then
            assertThrows(NullPointerException.class, () -> jwtService.createTokenPair(jwtPayload));
        }

    }

    @DisplayName("accessToken 재발급은")
    @Nested
    class Context_refreshAccessToken {

        @DisplayName("accessToken, refreshToken이 정확히 일치해야 재발급 받을 수 있다.")
        @Test
        void memberId_accessToken_refreshToken_isEquals_willSuccess() {

            // given
            Date date = new Date();
            JwtPayload jwtPayload = new JwtPayload("test@email.com", date);
            TokenPair tokenPair = jwtService.createTokenPair(jwtPayload);

            // when
            JwtPair refreshedJwtPair = jwtService.refreshAccessToken(
                new RefreshTokenRequest(tokenPair.accessToken(), tokenPair.refreshToken()));

            // then
            assertEquals(refreshedJwtPair.refreshToken(), tokenPair.refreshToken());

            JwtParser parser = Jwts.parser().verifyWith(secretKey).build();
            assertDoesNotThrow(() -> parser.parseSignedClaims(refreshedJwtPair.accessToken()));
        }

        @DisplayName("refreshToken이 만료되었다면 실패한다.")
        @Test
        void refreshToken_isNotEquals_willThrow_TokenExpiredException() {

            // given
            JwtPayload jwtPayload = new JwtPayload("test@email.com", new Date(0));
            TokenPair tokenPair = jwtService.createTokenPair(jwtPayload);

            // when then
            assertThrows(TokenExpiredException.class, () -> jwtService.refreshAccessToken(
                new RefreshTokenRequest(tokenPair.accessToken(), tokenPair.refreshToken())));
        }

        @DisplayName("가장 최근에 발급된 accessToken이 아니면 실패한다.")
        @Test
        void accessToken_notEquals_willThrow_BadTokenException() {

            // given
            JwtPayload jwtPayload = new JwtPayload("test@email.com", new Date(System.currentTimeMillis()));
            JwtPayload latestJwtPayload = new JwtPayload("test@email.com", new Date(System.currentTimeMillis() + 10000));
            TokenPair tokenPair = jwtService.createTokenPair(jwtPayload);
            TokenPair latestTokenPair = jwtService.createTokenPair(latestJwtPayload);

            // when then
            assertThrows(BadTokenException.class, () -> jwtService.refreshAccessToken(
                new RefreshTokenRequest(tokenPair.accessToken(), latestTokenPair.refreshToken())));
        }
    }
}
