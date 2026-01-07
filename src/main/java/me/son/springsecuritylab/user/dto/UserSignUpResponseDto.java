package me.son.springsecuritylab.user.dto;

import lombok.Builder;
import lombok.Getter;

import me.son.springsecuritylab.user.domain.entity.User;
import me.son.springsecuritylab.user.domain.entity.enums.Role;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserSignUpResponseDto {
    private Long id;
    private String username;
    private Role role;
    private String email;
    private LocalDateTime createdAt;

    public static UserSignUpResponseDto from(User user) {
        return UserSignUpResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build()
                ;
    }
}
