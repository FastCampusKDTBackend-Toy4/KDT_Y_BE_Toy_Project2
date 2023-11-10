package com.kdt_y_be_toy_project2.global.jwt;

import com.kdt_y_be_toy_project2.global.jwt.exception.BadTokenException;
import com.kdt_y_be_toy_project2.global.jwt.exception.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Objects;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private static final String USER_KEY = "user-key";

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;

    public JwtProvider(@Value("${service.jwt.secret-key}") String rawSecretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(rawSecretKey));
    }

    public String createAccessToken(JwtPayload jwtPayload) {
        return createToken(jwtPayload, accessExpiration);
    }

    private String createToken(JwtPayload jwtPayload, long expiration) {
        return Jwts.builder()
            .claim(USER_KEY, Objects.requireNonNull(jwtPayload.email()))
            .issuer(issuer)
            .issuedAt(Objects.requireNonNull(jwtPayload.issuedAt()))
            .expiration(new Date(jwtPayload.issuedAt().getTime() + expiration))
            .signWith(secretKey, Jwts.SIG.HS512)
            .compact();
    }

    public JwtPayload verifyToken(String jwtToken) {
        try {
            Jws<Claims> claimsJws = Jwts.parser().verifyWith(secretKey).build()
                .parseSignedClaims(jwtToken);
            Claims payload = claimsJws.getPayload();

            return new JwtPayload(payload.get(USER_KEY, String.class), payload.getIssuedAt());
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException(e.getMessage());
        } catch (JwtException e) {
            throw new BadTokenException(e.getMessage());
        }
    }
}
