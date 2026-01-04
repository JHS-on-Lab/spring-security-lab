package me.son.springsecuritylab.auth.domain.service;

import me.son.springsecuritylab.auth.dto.JwtDto;
import me.son.springsecuritylab.global.security.CustomUserDetails;
import me.son.springsecuritylab.user.domain.entity.enums.Role;

public interface AuthService {
    /**
     * 사용자 아이디와 비밀번호를 이용해 인증을 수행한다.
     *
     * @param username 로그인에 사용되는 사용자 식별자
     * @param password 사용자가 입력한 평문 비밀번호
     * @return 인증이 완료된 사용자 정보
     */
    CustomUserDetails authenticate(String username, String password);

    /**
     * 인증된 사용자 정보를 기반으로 JWT 토큰을 생성한다.
     *
     * @param username 토큰에 포함될 사용자 식별자
     * @param role     토큰 클레임에 포함될 사용자 권한 정보
     * @return 생성된 JWT 토큰 정보
     */
    JwtDto createTokensByUser(String username, Role role);
}
