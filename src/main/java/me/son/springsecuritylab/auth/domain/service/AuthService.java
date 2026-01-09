package me.son.springsecuritylab.auth.domain.service;

import me.son.springsecuritylab.global.security.CustomUserDetails;

public interface AuthService {
    /**
     * 사용자 아이디와 비밀번호를 이용해 인증을 수행한다.
     *
     * @param username 로그인에 사용되는 사용자 식별자
     * @param password 사용자가 입력한 평문 비밀번호
     * @return 인증이 완료된 사용자 정보
     */
    CustomUserDetails authenticate(String username, String password);
}
