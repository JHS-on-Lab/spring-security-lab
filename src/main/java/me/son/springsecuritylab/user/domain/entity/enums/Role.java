package me.son.springsecuritylab.user.domain.entity.enums;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    public String getRole() {
        return name();
    }
}
