package me.son.springsecuritylab.auth.domain.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import me.son.springsecuritylab.auth.domain.service.AuthService;
import me.son.springsecuritylab.auth.exception.AuthErrorCode;
import me.son.springsecuritylab.global.exception.BusinessException;
import me.son.springsecuritylab.global.security.CustomUserDetails;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ObjectProvider<AuthenticationManager> authenticationManagerProvider;

    @Override
    public CustomUserDetails authenticate(String username, String password) {
        try {
            AuthenticationManager authenticationManager = authenticationManagerProvider.getObject();
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return (CustomUserDetails) authentication.getPrincipal();
        } catch (BadCredentialsException e) {
            throw new BusinessException(AuthErrorCode.INVALID_CREDENTIALS);
        } catch (AuthenticationException e) {
            throw new BusinessException(AuthErrorCode.UNAUTHORIZED);
        }
    }
}
