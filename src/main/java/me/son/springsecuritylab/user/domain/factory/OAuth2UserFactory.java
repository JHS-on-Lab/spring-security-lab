package me.son.springsecuritylab.user.domain.factory;

import lombok.RequiredArgsConstructor;

import me.son.springsecuritylab.user.domain.entity.User;
import me.son.springsecuritylab.user.domain.entity.enums.Provider;
import me.son.springsecuritylab.user.domain.entity.enums.Role;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2UserFactory {
    private final PasswordEncoder passwordEncoder;

    public User create(Provider provider, String providerUserId, String email) {
        String username = provider.name().toLowerCase() + "_" + providerUserId;
        String password = passwordEncoder.encode(UUID.randomUUID().toString());

        return User.of(username, password, Role.ROLE_USER, email);
    }
}
