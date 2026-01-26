package me.son.springsecuritylab.auth.oauth2.exception;

import me.son.springsecuritylab.user.domain.entity.enums.Provider;

public class OAuth2LinkRequiredException extends RuntimeException {
    private final Provider provider;
    private final String email;

    public OAuth2LinkRequiredException(Provider provider, String email) {
        super("OAuth2 account linking required");
        this.provider = provider;
        this.email = email;
    }

    public Provider getProvider() {
        return provider;
    }

    public String getEmail() {
        return email;
    }
}
