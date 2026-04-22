package com.dearfloral.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateAccessToken(UserPrincipal principal) {
        Instant now = Instant.now();
        Instant expiredAt = now.plus(jwtProperties.accessTokenTtlMinutes(), ChronoUnit.MINUTES);
        return Jwts.builder()
                .subject(principal.getUsername())
                .issuer(jwtProperties.issuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiredAt))
                .claim("uid", principal.getUserId())
                .claim("role", principal.getRole())
                .signWith(getSigningKey())
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {
        Claims claims = parseToken(token);
        return claims.getExpiration() != null
                && claims.getExpiration().toInstant().isAfter(Instant.now())
                && jwtProperties.issuer().equals(claims.getIssuer());
    }

    public UserPrincipal toPrincipal(String token) {
        Claims claims = parseToken(token);
        Long userId = claims.get("uid", Number.class).longValue();
        String email = claims.getSubject();
        String role = claims.get("role", String.class);
        return new UserPrincipal(userId, email, "", role, "ACTIVE");
    }

    private SecretKey getSigningKey() {
        String secret = jwtProperties.secret();

        byte[] secretBytes = tryDecodeBase64UrlOrBase64(secret);
        if (secretBytes == null) {
            secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        }

        return Keys.hmacShaKeyFor(secretBytes);
    }

    private byte[] tryDecodeBase64UrlOrBase64(String secret) {
        try {
            return Decoders.BASE64URL.decode(secret);
        } catch (DecodingException ex) {
            // Fallback to BASE64 then plain-text bytes.
        }

        try {
            return Decoders.BASE64.decode(secret);
        } catch (DecodingException ex) {
            return null;
        }
    }
}
