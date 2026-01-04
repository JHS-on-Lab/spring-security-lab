package me.son.springsecuritylab.user.mapper;

import me.son.springsecuritylab.user.domain.entity.User;
import me.son.springsecuritylab.user.dto.UserRequestDto;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {

    /**
     * 사용자 등록 요청 DTO 를 User Entity 로 변환한다.
     *
     * @param dto             사용자 등록에 필요한 정보
     * @param passwordEncoder 비밀번호 해시 처리를 위한 PasswordEncoder
     * @return User Entity
     */
    public static User toEntity(UserRequestDto dto, PasswordEncoder passwordEncoder) {
        return User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .provider(dto.getProvider())
                .email(dto.getEmail())
                .build();
    }
}
