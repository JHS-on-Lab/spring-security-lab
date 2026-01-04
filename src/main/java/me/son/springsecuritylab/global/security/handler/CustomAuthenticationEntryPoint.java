package me.son.springsecuritylab.global.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

import me.son.springsecuritylab.auth.exception.AuthErrorCode;
import me.son.springsecuritylab.auth.jwt.exception.CustomJwtException;
import me.son.springsecuritylab.auth.jwt.exception.JwtErrorCode;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Log4j2
@Component
public class CustomAuthenticationEntryPoint extends AbstractSecurityErrorHandler implements AuthenticationEntryPoint {

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Object jwtExObj = request.getAttribute("JWT_EXCEPTION");

        if (jwtExObj instanceof CustomJwtException jwtException) {
            JwtErrorCode errorCode = jwtException.getErrorCode();
            log.warn("JWT Error: {}", errorCode.getMessage());
            writeErrorResponse(response, errorCode.getStatus().value(), errorCode.getMessage());
        } else {
            // 일반 인증 실패 (토큰 없음, 잘못된 인증 등)
            log.warn("Auth Error: {}", authException.getMessage());
            AuthErrorCode errorCode = AuthErrorCode.UNAUTHORIZED;
            writeErrorResponse(response, errorCode.getStatus().value(), errorCode.getMessage());
        }
    }
}
