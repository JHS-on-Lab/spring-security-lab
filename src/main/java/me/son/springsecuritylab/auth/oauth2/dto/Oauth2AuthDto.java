package me.son.springsecuritylab.auth.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.son.springsecuritylab.user.domain.entity.enums.Role;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Oauth2AuthDto {
    private Long id;
    private String username;
    private Role role;
}
