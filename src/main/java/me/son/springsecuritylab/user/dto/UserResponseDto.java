package me.son.springsecuritylab.user.dto;

import lombok.Getter;
import me.son.springsecuritylab.user.domain.entity.User;
import me.son.springsecuritylab.user.domain.entity.enums.Provider;
import me.son.springsecuritylab.user.domain.entity.enums.Role;

@Getter
public class UserResponseDto {
    private final String username;
    private final String password;
    private final Role role;
    private final Provider provider;
    private final String email;

    private UserResponseDto(String username, String password, Role role, Provider provider, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.email = email;
    }

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getProvider(),
                user.getEmail()
        );
    }
}
