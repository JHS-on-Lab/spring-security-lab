package me.son.springsecuritylab.global.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.log4j.Log4j2;

import me.son.springsecuritylab.auth.exception.AuthErrorCode;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Log4j2
@Component
public class CustomAccessDeniedHandler extends AbstractSecurityErrorHandler implements AccessDeniedHandler {

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        AuthErrorCode errorCode = AuthErrorCode.ACCESS_DENIED;
        log.warn("[Access Denied] {}", accessDeniedException.getMessage());

        writeErrorResponse(response, errorCode.getStatus().value(), errorCode.getMessage());
    }
}
