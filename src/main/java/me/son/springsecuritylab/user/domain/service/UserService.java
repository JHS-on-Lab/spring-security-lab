package me.son.springsecuritylab.user.domain.service;

import me.son.springsecuritylab.user.dto.*;
import org.springframework.data.domain.Page;

public interface UserService {
    /**
     * 사용자 ID로 사용자 정보 단건 조회합니다.
     *
     * @param id 사용자 목록 조회 조건 및 페이징 정보
     * @return 페이징 정보가 포함된 사용자 목록
     */
    UserSearchResponseDto getUserById(Long id);

    /**
     * 사용자 목록을 페이징하여 조회한다.
     *
     * @param request 사용자 목록 조회 조건 및 페이징 정보
     * @return 페이징 정보가 포함된 사용자 목록
     */
    Page<UserSearchResponseDto> getUsers(UserSearchRequestDto request);

    /**
     * 사용자명으로 사용자 정보 단건 조회합니다.
     *
     * @param username 사용자명
     * @return 조회된 사용자 정보
     */
    UserSearchResponseDto getUserByUsername(String username);

    /**
     * 새로운 사용자를 등록한다.
     *
     * @param request 사용자 등록에 필요한 정보
     * @return 등록된 사용자 정보
     */
    UserSignUpResponseDto addUser(UserSignUpRequestDto request);

    /**
     * 로그인한 사용자 정보를 조회힙니다.
     *
     * @param id 사용자 ID
     * @return 조회된 사용자 정보
     */
    UserMeResponseDto getMyInfo(Long id);
}
