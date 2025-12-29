package me.son.springsecuritylab.user.mapper;

import me.son.springsecuritylab.user.domain.entity.User;
import me.son.springsecuritylab.user.dto.UserRequestDto;

public class UserMapper {

    /**
     * 사용자 등록 요청 DTO 를 User Entity 로 변환한다.
     *
     * @param dto 사용자 등록에 필요한 정보
     * @return User Entity
     */
    public static User toEntity(UserRequestDto dto) {
        return User.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .role(dto.getRole())
                .provider(dto.getProvider())
                .email(dto.getEmail())
                .build();
    }
}
