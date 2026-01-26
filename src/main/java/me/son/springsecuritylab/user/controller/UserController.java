package me.son.springsecuritylab.user.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import me.son.springsecuritylab.global.paging.dto.PageResponseDto;
import me.son.springsecuritylab.global.response.ApiResponse;
import me.son.springsecuritylab.global.security.CustomUserDetails;
import me.son.springsecuritylab.user.domain.service.UserService;
import me.son.springsecuritylab.user.dto.*;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @SecurityRequirement(name = "BearerAuth")
    @GetMapping
    public ApiResponse<PageResponseDto<UserSearchResponseDto>> getUsers(@ModelAttribute UserSearchRequestDto request) {
        log.info("getUsers request: {}", request);
        Page<UserSearchResponseDto> page = userService.getUsers(request);
        return ApiResponse.success(PageResponseDto.from(page));
    }

    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/{username}")
    public ApiResponse<UserSearchResponseDto> getUserByUsername(@PathVariable String username) {
        log.info("getUserByUsername request username: {}", username);
        UserSearchResponseDto user = userService.getUserByUsername(username);
        return ApiResponse.success(user);
    }

    @PostMapping
    public ApiResponse<UserSignUpResponseDto> addUser(@RequestBody UserSignUpRequestDto request) {
        log.info("addUser request: {}", request);
        UserSignUpResponseDto user = userService.addUser(request);
        return ApiResponse.success(user);
    }

    @GetMapping("/me")
    public ApiResponse<UserMeResponseDto> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        log.info("getMyInfo request username: {}", userDetails.getUsername());
        return ApiResponse.success(userService.getMyInfo(userDetails.getId()));
    }
}
