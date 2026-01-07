package me.son.springsecuritylab.auth.jwt;

import io.jsonwebtoken.Claims;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import me.son.springsecuritylab.auth.jwt.dto.ParsedToken;
import me.son.springsecuritylab.auth.jwt.exception.CustomJwtException;
import me.son.springsecuritylab.global.security.CustomUserDetails;
import me.son.springsecuritylab.user.domain.entity.enums.Role;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        log.info("Access Token: {}", token);

        if (StringUtils.hasText(token)) {
            try {
                // 파싱 및 검증
                ParsedToken parsed = jwtProvider.parseToken(token);
                Long id = Long.valueOf(parsed.getSubject());
                Claims claims = parsed.getClaims();
                String username = claims.get("username", String.class);
                Role role = Role.valueOf(claims.get("role", String.class));
                log.info("id: {}, username: {}, role: {}", id, username, role);

                CustomUserDetails userDetails = new CustomUserDetails(id, username, role);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (CustomJwtException e) {
                // Security 에서 예외 처리
                SecurityContextHolder.clearContext();
                request.setAttribute("JWT_EXCEPTION", e);
            }
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
