package me.son.springsecuritylab.user.domain.repository;

import me.son.springsecuritylab.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
}
