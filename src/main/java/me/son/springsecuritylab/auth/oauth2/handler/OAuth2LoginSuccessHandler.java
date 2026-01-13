package me.son.springsecuritylab.auth.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import me.son.springsecuritylab.auth.dto.JwtDto;
import me.son.springsecuritylab.auth.jwt.JwtService;
import me.son.springsecuritylab.user.domain.entity.User;
import me.son.springsecuritylab.user.domain.entity.UserIdentity;
import me.son.springsecuritylab.user.domain.entity.enums.Provider;
import me.son.springsecuritylab.user.domain.factory.OAuth2UserFactory;
import me.son.springsecuritylab.user.domain.repository.UserIdentityRepository;
import me.son.springsecuritylab.user.domain.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static me.son.springsecuritylab.global.util.CookieUtil.addHttpOnlyCookie;

@Log4j2
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserIdentityRepository userIdentityRepository;
    private final UserRepository userRepository;
    private final OAuth2UserFactory oAuth2UserFactory;
    private final JwtService jwtService;

    @Value("${jwt.expiration.access}")
    private long accessTokenExpirationMs;
    @Value("${jwt.expiration.refresh}")
    private long refreshTokenExpirationMs;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;

        Provider provider = Provider.valueOf(token.getAuthorizedClientRegistrationId().toUpperCase());

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String providerUserId;
        switch (provider) {
            case GOOGLE -> providerUserId = oAuth2User.getAttribute("sub");
            case KAKAO, GITHUB -> providerUserId = oAuth2User.getAttribute("id").toString();
            default -> throw new IllegalStateException("Unsupported provider");
        }

        String email = oAuth2User.getAttribute("email");

        Optional<UserIdentity> userIdentity =
                userIdentityRepository.findByProviderAndProviderUserId(
                        provider,
                        providerUserId
                );

        User user;


        if (userIdentity.isPresent()) {
            // 로그인 성공
            user = userIdentity.get().getUser();
        } else if (email != null && userRepository.existsByEmail(email)) {
            // identity 없음 + email 중복 → 계정 연동 제안
            response.sendRedirect("/oauth2/link-required");
            return;
        } else {
            // 신규 → User + UserIdentity 생성
            user = userRepository.save(oAuth2UserFactory.create(provider, providerUserId, email));
            userIdentityRepository.save(UserIdentity.of(user, provider, providerUserId));
        }

        JwtDto tokens = jwtService.createTokens(user.getId(), user.getUsername(), user.getRole());

        addHttpOnlyCookie(response, "refreshToken", tokens.getRefreshToken());

        response.sendRedirect("/oauth2/success#accessToken=" + tokens.getAccessToken());
    }
}
