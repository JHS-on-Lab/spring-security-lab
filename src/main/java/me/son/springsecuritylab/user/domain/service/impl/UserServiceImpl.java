package me.son.springsecuritylab.user.domain.service.impl;

import lombok.RequiredArgsConstructor;

import me.son.springsecuritylab.global.exception.BusinessException;
import me.son.springsecuritylab.user.domain.entity.User;
import me.son.springsecuritylab.user.domain.entity.UserIdentity;
import me.son.springsecuritylab.user.domain.entity.enums.Provider;
import me.son.springsecuritylab.user.domain.repository.UserIdentityRepository;
import me.son.springsecuritylab.user.domain.repository.UserRepository;
import me.son.springsecuritylab.user.domain.service.UserService;
import me.son.springsecuritylab.user.dto.UserSearchRequestDto;
import me.son.springsecuritylab.user.dto.UserSignUpRequestDto;
import me.son.springsecuritylab.user.dto.UserSearchResponseDto;
import me.son.springsecuritylab.user.dto.UserSignUpResponseDto;
import me.son.springsecuritylab.user.exception.UserErrorCode;
import me.son.springsecuritylab.user.mapper.UserMapper;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserIdentityRepository userIdentityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserSearchResponseDto> getUsers(UserSearchRequestDto request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize()
        );

        return userRepository.findAll(pageable)
                .map(UserSearchResponseDto::from);
    }

    @Override
    public UserSearchResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
        return UserSearchResponseDto.from(user);
    }

    @Override
    @Transactional
    public UserSignUpResponseDto addUser(UserSignUpRequestDto request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BusinessException(UserErrorCode.DUPLICATE_USERNAME);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(UserErrorCode.DUPLICATE_EMAIL);
        }

        try {
            // 1. User 생성
            User user = userRepository.save(UserMapper.toEntity(request, passwordEncoder));

            // 2. LOCAL UserIdentity 생성
            UserIdentity userIdentity = UserIdentity.of(user, Provider.LOCAL, request.getUsername());

            userIdentityRepository.save(userIdentity);

            return UserSignUpResponseDto.from(user);
        } catch (DataIntegrityViolationException e) {
            // username / email unique 제약 위반
            throw new BusinessException(UserErrorCode.DUPLICATE_USER);
        }
    }
}
