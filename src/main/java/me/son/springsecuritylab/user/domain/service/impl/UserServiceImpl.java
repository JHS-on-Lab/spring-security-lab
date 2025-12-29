package me.son.springsecuritylab.user.domain.service.impl;

import lombok.RequiredArgsConstructor;

import me.son.springsecuritylab.user.domain.entity.User;
import me.son.springsecuritylab.user.domain.repository.UserRepository;
import me.son.springsecuritylab.user.domain.service.UserService;
import me.son.springsecuritylab.user.dto.UserRequestDto;
import me.son.springsecuritylab.user.dto.UserResponseDto;
import me.son.springsecuritylab.user.mapper.UserMapper;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Page<UserResponseDto> getUsers(UserRequestDto request) {
        try {
            Pageable pageable = PageRequest.of(
                    request.getPage(),
                    request.getSize()
            );

            return userRepository.findAll(pageable)
                    .map(UserResponseDto::from);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public UserResponseDto addUser(UserRequestDto request) {
        try {
            User user = userRepository.save(UserMapper.toEntity(request));
            return UserResponseDto.from(user);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
