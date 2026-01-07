package me.son.springsecuritylab.user.domain.repository;

import me.son.springsecuritylab.user.domain.entity.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserIdentityRepository extends JpaRepository<UserIdentity, Long> {
}
