package me.son.springsecuritylab.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import me.son.springsecuritylab.global.paging.dto.PageResponseDto;
import me.son.springsecuritylab.global.response.ApiResponse;
import me.son.springsecuritylab.user.domain.service.UserService;
import me.son.springsecuritylab.user.dto.UserRequestDto;
import me.son.springsecuritylab.user.dto.UserResponseDto;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ApiResponse<PageResponseDto<UserResponseDto>> getUsers(@ModelAttribute UserRequestDto request) {
        log.info("getUsers request: {}", request);
        Page<UserResponseDto> page = userService.getUsers(request);
        return ApiResponse.success(PageResponseDto.from(page));
    }

    @GetMapping("/{username}")
    public ApiResponse<UserResponseDto> getUserByUsername(@PathVariable String username) {
        log.info("getUserByUsername request username: {}", username);
        UserResponseDto user = userService.getUserByUsername(username);
        return ApiResponse.success(user);
    }

    @PostMapping
    public ApiResponse<UserResponseDto> addUser(@RequestBody UserRequestDto request) {
        log.info("addUser request: {}", request);
        UserResponseDto user = userService.addUser(request);
        return ApiResponse.success(user);
    }
}
