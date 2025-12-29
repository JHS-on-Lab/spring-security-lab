package me.son.springsecuritylab.user.domain.entity.enums;

public enum Provider {
    KAKAO,
    GOOGLE;

    public String getProvider() {
        return name();
    }
}
