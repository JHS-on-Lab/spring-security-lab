package me.son.springsecuritylab.auth.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class AuthRequestDto {
    private String username;
    private String password;
}
