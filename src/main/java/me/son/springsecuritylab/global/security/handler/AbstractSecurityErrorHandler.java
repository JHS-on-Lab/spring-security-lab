package me.son.springsecuritylab.global.security.handler;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import me.son.springsecuritylab.global.response.ApiResponse;
import org.springframework.http.MediaType;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@RequiredArgsConstructor
public abstract class AbstractSecurityErrorHandler {
    protected final ObjectMapper objectMapper;

    protected void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {

        if (response.isCommitted()) {
            return;
        }

        ApiResponse<Void> errorResponse = ApiResponse.failure(message);

        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
