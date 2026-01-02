package me.son.springsecuritylab.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;

import me.son.springsecuritylab.auth.jwt.dto.ParsedToken;

import me.son.springsecuritylab.auth.jwt.exception.CustomJwtException;
import me.son.springsecuritylab.auth.jwt.exception.JwtErrorCode;
import me.son.springsecuritylab.user.domain.entity.enums.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = SECRET_KEY.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("Secret key must be at least 256 bits (32 bytes) for HS256.");
        }
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(String username, Role role, long tokenExpirationMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + tokenExpirationMs);
        return Jwts.builder().subject(username)
                .claim("role", role.getName()).issuedAt(now).expiration(expiry)
                .signWith(key)
                .compact();
    }

    public ParsedToken parseToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new CustomJwtException(JwtErrorCode.JWT_NOT_FOUND);
        }
        try {
            Jws<Claims> jws = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return ParsedToken.builder()
                    .subject(jws.getPayload().getSubject())
                    .claims(jws.getPayload())
                    .build()
                    ;
        } catch (ExpiredJwtException e) {
            throw new CustomJwtException(JwtErrorCode.JWT_EXPIRED);
        } catch (UnsupportedJwtException e) {
            throw new CustomJwtException(JwtErrorCode.JWT_UNSUPPORTED);
        } catch (MalformedJwtException e) {
            throw new CustomJwtException(JwtErrorCode.JWT_MALFORMED);
        } catch (Exception e) {
            throw new CustomJwtException(JwtErrorCode.JWT_UNKNOWN_ERROR);
        }
    }
}
