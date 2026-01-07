package me.son.springsecuritylab.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import me.son.springsecuritylab.user.domain.entity.enums.Role;

@Setter // @ModelAttribute 사용 시 필수
@Getter
@ToString
public class UserSignUpRequestDto {
    private String username;
    private String password;
    private Role role;
    private String email;
}
