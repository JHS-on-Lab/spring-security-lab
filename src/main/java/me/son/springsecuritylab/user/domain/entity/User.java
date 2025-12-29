package me.son.springsecuritylab.user.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import me.son.springsecuritylab.user.domain.entity.enums.Provider;
import me.son.springsecuritylab.user.domain.entity.enums.Role;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
public class User {
    @Id
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(unique = true)
    private String email;

    protected User() {}
}
