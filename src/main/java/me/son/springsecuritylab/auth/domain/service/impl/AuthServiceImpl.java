package me.son.springsecuritylab.auth.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import me.son.springsecuritylab.auth.domain.service.AuthService;
import me.son.springsecuritylab.auth.dto.JwtDto;
import me.son.springsecuritylab.auth.exception.AuthErrorCode;
import me.son.springsecuritylab.auth.jwt.JwtProvider;
import me.son.springsecuritylab.global.exception.BusinessException;
import me.son.springsecuritylab.global.security.CustomUserDetails;
import me.son.springsecuritylab.user.domain.entity.enums.Role;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Value("${jwt.expiration.access}")
    private long accessTokenExpirationMs;
    @Value("${jwt.expiration.refresh}")
    private long refreshTokenExpirationMs;

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    public CustomUserDetails authenticate(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return (CustomUserDetails) authentication.getPrincipal();
        } catch (BadCredentialsException e) {
            throw new BusinessException(AuthErrorCode.INVALID_CREDENTIALS);
        } catch (AuthenticationException e) {
            throw new BusinessException(AuthErrorCode.UNAUTHORIZED);
        }
    }

    @Override
    public JwtDto createTokensByUser(String username, Role role) {
        return JwtDto.builder()
                .accessToken(jwtProvider.createToken(username, role, accessTokenExpirationMs))
                .refreshToken(jwtProvider.createToken(username, role, refreshTokenExpirationMs))
                .build()
                ;
    }
}
