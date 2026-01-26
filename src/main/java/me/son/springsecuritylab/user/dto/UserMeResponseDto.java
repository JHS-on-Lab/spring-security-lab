package me.son.springsecuritylab.user.dto;

import lombok.Builder;
import lombok.Getter;

import me.son.springsecuritylab.user.domain.entity.User;
import me.son.springsecuritylab.user.domain.entity.UserIdentity;
import me.son.springsecuritylab.user.domain.entity.enums.Provider;
import me.son.springsecuritylab.user.domain.entity.enums.Role;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class UserMeResponseDto {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private LocalDateTime createdAt;

    private List<Provider> connectedProviders;

    public static UserMeResponseDto from(User user) {
        return UserMeResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .connectedProviders(user.getIdentities().stream()
                        .map(UserIdentity::getProvider)
                        .toList())
                .build()
                ;
    }

}
