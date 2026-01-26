package me.son.springsecuritylab.auth.oauth2.service;

import lombok.RequiredArgsConstructor;
import me.son.springsecuritylab.auth.oauth2.dto.Oauth2AuthDto;
import me.son.springsecuritylab.auth.oauth2.exception.OAuth2LinkRequiredException;
import me.son.springsecuritylab.user.domain.entity.User;
import me.son.springsecuritylab.user.domain.entity.UserIdentity;
import me.son.springsecuritylab.user.domain.entity.enums.Provider;
import me.son.springsecuritylab.user.domain.factory.OAuth2UserFactory;
import me.son.springsecuritylab.user.domain.repository.UserIdentityRepository;
import me.son.springsecuritylab.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OAuth2AuthService {

    private final UserIdentityRepository userIdentityRepository;
    private final UserRepository userRepository;
    private final OAuth2UserFactory oAuth2UserFactory;

    public Oauth2AuthDto handleLogin(Provider provider, String providerUserId, String email) {
        // 이미 연동된 계정
        return userIdentityRepository
                .findByProviderAndProviderUserId(provider, providerUserId)
                .map(UserIdentity::getUser)
                .map(this::toDto)
                .orElseGet(() -> handleNewOrLinkRequired(provider, providerUserId, email));
    }

    private Oauth2AuthDto handleNewOrLinkRequired(Provider provider, String providerUserId, String email) {
        // email 중복 → 계정 연동 필요
        if (email != null && userRepository.existsByEmail(email)) {
            throw new OAuth2LinkRequiredException(provider, email);
        }

        // 완전 신규 사용자
        User user = userRepository.save(oAuth2UserFactory.create(provider, providerUserId, email));

        userIdentityRepository.save(UserIdentity.of(user, provider, providerUserId));

        return toDto(user);
    }

    private Oauth2AuthDto toDto(User user) {
        return new Oauth2AuthDto(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
