package me.son.springsecuritylab.user.domain.service.impl;

import lombok.RequiredArgsConstructor;

import me.son.springsecuritylab.global.exception.BusinessException;
import me.son.springsecuritylab.user.domain.entity.User;
import me.son.springsecuritylab.user.domain.repository.UserRepository;
import me.son.springsecuritylab.user.domain.service.UserService;
import me.son.springsecuritylab.user.dto.UserRequestDto;
import me.son.springsecuritylab.user.dto.UserResponseDto;
import me.son.springsecuritylab.user.exception.UserErrorCode;
import me.son.springsecuritylab.user.mapper.UserMapper;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponseDto> getUsers(UserRequestDto request) {
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize()
        );

        return userRepository.findAll(pageable)
                .map(UserResponseDto::from);
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findById(username).orElseThrow(() -> new BusinessException(UserErrorCode.USER_NOT_FOUND));
        return UserResponseDto.from(user);
    }

    @Override
    public UserResponseDto addUser(UserRequestDto request) {
        try {
            User user = userRepository.save(UserMapper.toEntity(request, passwordEncoder));
            return UserResponseDto.from(user);
        } catch (DataIntegrityViolationException e) {
            // username / email unique 제약 위반
            throw new BusinessException(UserErrorCode.DUPLICATE_USER);
        }
    }
}
