package me.son.springsecuritylab.auth.controller;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import me.son.springsecuritylab.auth.domain.service.AuthService;
import me.son.springsecuritylab.auth.dto.AuthRequestDto;
import me.son.springsecuritylab.auth.dto.JwtDto;
import me.son.springsecuritylab.auth.jwt.service.JwtService;
import me.son.springsecuritylab.global.response.ApiResponse;
import me.son.springsecuritylab.global.security.CustomUserDetails;
import me.son.springsecuritylab.user.domain.service.UserService;
import me.son.springsecuritylab.user.dto.UserSearchResponseDto;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static me.son.springsecuritylab.global.util.CookieUtil.addHttpOnlyCookie;

@Log4j2
@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/sign-in")
    public ApiResponse<String> signIn(@RequestBody AuthRequestDto request, HttpServletResponse response) {
        log.info("signIn request: {}", request.toString());
        CustomUserDetails user = authService.authenticate(request.getUsername(), request.getPassword());

        JwtDto tokens = jwtService.createTokens(user.getId(), user.getUsername(), user.getRole());
        // Refresh Token 은 HTTP Only Cookie 저장
        addHttpOnlyCookie(response, "refreshToken", tokens.getRefreshToken());

        return ApiResponse.success(tokens.getAccessToken());
    }

    @PostMapping("/reissue")
    public ApiResponse<String> reissue(@CookieValue(value = "refreshToken") String refreshToken, HttpServletResponse response) {
        log.info("reissue refreshToken: {}", refreshToken);

        Long id = jwtService.getSubject(refreshToken);
        // "DB 접속 최소화"보다 "토큰 재발급의 신뢰성"이 더 중요하다 판단되어 User 정보 재조회
        UserSearchResponseDto user = userService.getUserById(id);

        JwtDto tokens = jwtService.createTokens(user.getId(), user.getUsername(), user.getRole());
        // Refresh Token 은 HTTP Only Cookie 저장
        addHttpOnlyCookie(response, "refreshToken", tokens.getRefreshToken());

        return ApiResponse.success(tokens.getAccessToken());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/oauth2/link/{provider}")
    public ApiResponse<String> createOAuth2Link(@PathVariable String provider, @AuthenticationPrincipal CustomUserDetails user) {
        String url = "http://localhost:8080/oauth2/authorization/" + provider.toLowerCase() + "?id=" + user.getId();
        return ApiResponse.success(url);
    }

}
