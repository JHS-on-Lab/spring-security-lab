package me.son.springsecuritylab.auth.jwt.exception;

import lombok.Getter;

@Getter
public class CustomJwtException extends RuntimeException {
    private final JwtErrorCode errorCode;

    public CustomJwtException(JwtErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
