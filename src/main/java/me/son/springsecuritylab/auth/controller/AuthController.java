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

    @PostMapping("/sign-in")
    public ApiResponse<AuthResponseDto> signIn(@RequestBody AuthRequestDto request, HttpServletResponse response) {
        log.info("signIn request: {}", request.toString());
        CustomUserDetails user = authService.authenticate(request.getUsername(), request.getPassword());

        JwtDto tokens = authService.createTokensByUser(user.getId(), user.getUsername(), user.getRole());
        // Refresh Token 은 HTTP Only Cookie 저장
        addHttpOnlyCookie(response, "refreshToken", tokens.getRefreshToken());

        return ApiResponse.success(AuthResponseDto.builder()
                .accessToken(tokens.getAccessToken())
                .username(user.getUsername())
                .build())
                ;
    }

    @PostMapping("/reissue")
    public ApiResponse<AuthResponseDto> reissue(@CookieValue(value = "refreshToken") String refreshToken, HttpServletResponse response) {
        log.info("reissue refreshToken: {}", refreshToken);
        // refresh token 유효성 검사
        CustomUserDetails user = authService.validateToken(refreshToken);

        JwtDto tokens = authService.createTokensByUser(user.getId(), user.getUsername(), user.getRole());
        // Refresh Token 은 HTTP Only Cookie 저장
        addHttpOnlyCookie(response, "refreshToken", tokens.getRefreshToken());

        return ApiResponse.success(AuthResponseDto.builder()
                .accessToken(tokens.getAccessToken())
                .username(user.getUsername())
                .build())
                ;
    }

}
