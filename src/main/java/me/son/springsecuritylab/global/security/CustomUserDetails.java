package me.son.springsecuritylab.global.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.son.springsecuritylab.user.domain.entity.enums.Provider;

import me.son.springsecuritylab.user.domain.entity.enums.Role;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder // Lombok의 @Builder 규칙: 클래스에 생성자가 하나라도 있으면 @Builder는 그 생성자만 기준으로 Builder를 만듦.
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private String username;
    private String password;
    @Getter
    private Role role;
    private Provider provider;
    private String email;

    public CustomUserDetails(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> role.name());
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
