package me.son.springsecuritylab.auth.controller;

import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import me.son.springsecuritylab.auth.domain.service.AuthService;
import me.son.springsecuritylab.auth.dto.AuthRequestDto;
import me.son.springsecuritylab.auth.dto.AuthResponseDto;
import me.son.springsecuritylab.auth.dto.JwtDto;
import me.son.springsecuritylab.auth.jwt.JwtService;
import me.son.springsecuritylab.global.response.ApiResponse;
import me.son.springsecuritylab.global.security.CustomUserDetails;

import me.son.springsecuritylab.user.domain.service.UserService;
import me.son.springsecuritylab.user.dto.UserSearchResponseDto;
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
    public ApiResponse<AuthResponseDto> signIn(@RequestBody AuthRequestDto request, HttpServletResponse response) {
        log.info("signIn request: {}", request.toString());
        CustomUserDetails user = authService.authenticate(request.getUsername(), request.getPassword());

        JwtDto tokens = jwtService.createTokens(user.getId(), user.getUsername(), user.getRole());
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
        /**
         * JwtFilter 에서 Access Token 이 만료되면 Client 에서 Reissue API 를 호출함.
         * 이 때 Cookie 에 있는 Refresh Token 을 검증하여 Subject(ID)를 추출하여 User 를 재조회 함.
         * User 를 재조회하는 행위는 필수는 아니나, 보안을 위해 전략적으로 선택함.
         *
         * DB 접속을 최소한으로 하고싶다면? Refresh Token Claims 에도 User 의 부가정보를 넣어 해당 정보로 새 ToKen 을 발행.
         * TODO. 더 좋은 방법이 있을까?
         */
        Long id = jwtService.getSubject(refreshToken);
        UserSearchResponseDto user = userService.getUserById(id);

        JwtDto tokens = jwtService.createTokens(user.getId(), user.getUsername(), user.getRole());
        // Refresh Token 은 HTTP Only Cookie 저장
        addHttpOnlyCookie(response, "refreshToken", tokens.getRefreshToken());

        return ApiResponse.success(AuthResponseDto.builder()
                .accessToken(tokens.getAccessToken())
                .username(user.getUsername())
                .build())
                ;
    }

}
