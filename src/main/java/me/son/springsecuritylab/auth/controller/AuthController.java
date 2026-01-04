package me.son.springsecuritylab.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import me.son.springsecuritylab.auth.domain.service.AuthService;
import me.son.springsecuritylab.auth.dto.AuthRequestDto;
import me.son.springsecuritylab.auth.dto.AuthResponseDto;
import me.son.springsecuritylab.auth.dto.JwtDto;
import me.son.springsecuritylab.global.response.ApiResponse;
import me.son.springsecuritylab.global.security.CustomUserDetails;

import org.springframework.web.bind.annotation.*;

import static me.son.springsecuritylab.global.util.CookieUtil.addHttpOnlyCookie;

@Log4j2
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponseDto> login(@RequestBody AuthRequestDto request, HttpServletResponse response) {
        log.info("login request: {}", request.toString());
        CustomUserDetails user = authService.authenticate(request.getUsername(), request.getPassword());

        JwtDto tokens = authService.createTokensByUser(user.getUsername(), user.getRole());
        // Refresh Token 은 HTTP Only Cookie 저장
        addHttpOnlyCookie(response, "refreshToken", tokens.getRefreshToken());

        return ApiResponse.success(AuthResponseDto.builder()
                .accessToken(tokens.getAccessToken())
                .username(user.getUsername())
                .build())
                ;
    }

}
