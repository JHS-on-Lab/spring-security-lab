package me.son.springsecuritylab.auth.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import me.son.springsecuritylab.auth.dto.JwtDto;
import me.son.springsecuritylab.auth.jwt.JwtService;
import me.son.springsecuritylab.auth.oauth2.dto.Oauth2AuthDto;
import me.son.springsecuritylab.auth.oauth2.exception.OAuth2LinkRequiredException;
import me.son.springsecuritylab.auth.oauth2.service.OAuth2AuthService;
import me.son.springsecuritylab.user.domain.entity.enums.Provider;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

import static me.son.springsecuritylab.global.util.CookieUtil.addHttpOnlyCookie;

@Log4j2
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthService oAuth2AuthService;
    private final JwtService jwtService;
    private static final String SPA_BASE_URL = "http://localhost:5173";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        Provider provider = Provider.valueOf(token.getAuthorizedClientRegistrationId().toUpperCase());

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println(oAuth2User);
        String providerUserId = extractProviderUserId(provider, oAuth2User);
        String email = oAuth2User.getAttribute("email");

        try {
            // OAuth2 로그인 정책은 전부 Service에 위임
            Oauth2AuthDto authDto = oAuth2AuthService.handleLogin(provider, providerUserId, email);

            // 로그인 성공 케이스만 JWT 발급
            JwtDto tokens = jwtService.createTokens(authDto.getId(), authDto.getUsername(), authDto.getRole());

            addHttpOnlyCookie(response, "refreshToken", tokens.getRefreshToken());
            response.sendRedirect(SPA_BASE_URL + "/oauth2/success#accessToken=" + tokens.getAccessToken());

        } catch (OAuth2LinkRequiredException e) {
            // 계정 연동 필요
            log.info("OAuth2 link required. provider={}, email={}", e.getProvider(), e.getEmail());

            response.sendRedirect(SPA_BASE_URL + "/oauth2/link-required");
        }
    }

    /**
     * Provider별 사용자 식별자 추출
     */
    private String extractProviderUserId(Provider provider, OAuth2User oAuth2User) {
        return switch (provider) {
            case GOOGLE -> oAuth2User.getAttribute("sub");
            case KAKAO, GITHUB -> {
                Object id = oAuth2User.getAttribute("id");
                if (id == null) {
                    throw new IllegalStateException("Missing provider user id");
                }
                yield id.toString();
            }
            default -> throw new IllegalStateException("Unsupported provider: " + provider);
        };
    }
}
