package me.son.springsecuritylab.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import me.son.springsecuritylab.global.dto.PageResponseDto;
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

    @GetMapping("")
    public ApiResponse<PageResponseDto<UserResponseDto>> getUsers(@ModelAttribute UserRequestDto request) {
        log.info("getUsers request : {}", request.toString());
        Page<UserResponseDto> page = userService.getUsers(request);
        return ApiResponse.success(PageResponseDto.from(page));
    }

    @PostMapping("")
    public ApiResponse<UserResponseDto> addUser(@RequestBody UserRequestDto request) {
        log.info("addUser request : {}", request.toString());
        UserResponseDto user = userService.addUser(request);
        return ApiResponse.success(user);
    }
}
