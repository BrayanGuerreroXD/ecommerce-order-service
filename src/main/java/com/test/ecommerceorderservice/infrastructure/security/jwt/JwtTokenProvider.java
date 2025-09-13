package com.test.ecommerceorderservice.infrastructure.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.test.ecommerceorderservice.infrastructure.web.dto.GenerateTokenResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String jwtSecret = "SuperSecretKeyParaJWT12345678901234567890"; // Debe ser seguro y largo
    private final long jwtExpirationInMs = 3600000; // 1 hora
    private final Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

    public GenerateTokenResponse generateToken(String id, String key) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        String token = JWT.create()
                .withSubject(id)
                .withClaim("key", key)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(algorithm);

        return new GenerateTokenResponse(
                token,
                LocalDateTime.now().plusHours(1)
        );
    }

    public boolean validateToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            if (isTokenExpired(jwt) || !isAlgorithmValid(jwt)) {
                return false;
            }
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            return false;
        }
    }

    public String getUserIdFromJWT(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getSubject();
    }

    public String getKeyFromJWT(String token) {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        return jwt.getClaim("key").asString();
    }

    public boolean isTokenExpired(DecodedJWT jwt) {
        return jwt.getExpiresAt().before(new Date());
    }

    private boolean isAlgorithmValid(DecodedJWT jwt) {
        return jwt.getAlgorithm().equals(algorithm.getName());
    }
}
