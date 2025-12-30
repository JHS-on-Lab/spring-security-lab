package me.son.springsecuritylab.auth.jwt.dto;

import io.jsonwebtoken.Claims;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParsedToken {
    private String subject;
    private Claims claims;
}
