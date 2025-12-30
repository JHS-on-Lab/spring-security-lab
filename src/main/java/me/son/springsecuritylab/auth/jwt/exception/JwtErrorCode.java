package me.son.springsecuritylab.auth.jwt.exception;

import me.son.springsecuritylab.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum JwtErrorCode implements ErrorCode {
    JWT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
    JWT_MALFORMED(HttpStatus.BAD_REQUEST,"잘못된 형식의 토큰입니다."),
    JWT_UNSUPPORTED(HttpStatus.UNAUTHORIZED,"지원하지 않는 토큰입니다."),
    JWT_INVALID(HttpStatus.UNAUTHORIZED,"유효하지 않은 토큰입니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED,"만료된 토큰입니다."),
    JWT_UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "JWT 처리 중 알 수 없는 오류가 발생했습니다.")
    ;

    private final HttpStatus status;
    private final String message;

    JwtErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
