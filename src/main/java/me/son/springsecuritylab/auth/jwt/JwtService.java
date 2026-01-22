package me.son.springsecuritylab.auth.jwt;

import io.jsonwebtoken.Claims;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import me.son.springsecuritylab.auth.dto.JwtDto;
import me.son.springsecuritylab.auth.jwt.dto.ParsedToken;
import me.son.springsecuritylab.auth.jwt.exception.CustomJwtException;
import me.son.springsecuritylab.auth.jwt.exception.JwtErrorCode;
import me.son.springsecuritylab.global.security.CustomUserDetails;
import me.son.springsecuritylab.user.domain.entity.enums.Role;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.expiration.access}")
    private long accessTokenExpirationMs;
    @Value("${jwt.expiration.refresh}")
    private long refreshTokenExpirationMs;

    private final JwtProvider jwtProvider;

    /**
     * 인증된 사용자 정보를 기반으로 JWT 토큰을 생성한다.
     *
     * @param id 토큰에 포함될 사용자 식별자
     * @param username 토큰 클레임에 포함될 사용자명
     * @param role     토큰 클레임에 포함될 사용자 권한 정보
     * @return 생성된 JWT 토큰 정보
     */
    public JwtDto createTokens(Long id, String username, Role role) {
        return JwtDto.builder()
                .accessToken(jwtProvider.createToken(id, username, role, accessTokenExpirationMs))
                .refreshToken(jwtProvider.createToken(id, refreshTokenExpirationMs))
                .build()
                ;
    }

    /**
     * JWT 토큰을 검증하여 토큰의 Subject(User's ID) 를 반환한다
     *
     * @param token JWT 문자열
     * @return 인증된 사용자 ID
     */
    public Long getSubject(String token) {
        try {
            ParsedToken parsed = jwtProvider.parseToken(token);
            return Long.valueOf(parsed.getSubject());
        } catch (Exception e) {
            throw new CustomJwtException(JwtErrorCode.JWT_INVALID);
        }
    }

    /**
     * JWT 토큰을 검증하여 CustomUserDetails 를 반환한다
     *
     * @param token JWT 문자열
     * @return 인증된 사용자 정보
     */
    public CustomUserDetails getCustomUserDetails(String token) {
        try {
            // 파싱 및 검증
            ParsedToken parsed = jwtProvider.parseToken(token);
            Long id = Long.valueOf(parsed.getSubject());
            Claims claims = parsed.getClaims();
            String username = claims.get("username", String.class);
            Role role = Role.valueOf(claims.get("role", String.class));
            log.info("id: {}, username: {}, role: {}", id, username, role);

            return new CustomUserDetails(id, username, role);
        } catch (CustomJwtException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomJwtException(JwtErrorCode.JWT_INVALID);
        }
    }
}
