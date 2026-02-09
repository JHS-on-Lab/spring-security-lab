package me.son.springsecuritylab.user.domain.repository;

import me.son.springsecuritylab.user.domain.entity.UserIdentity;
import me.son.springsecuritylab.user.domain.entity.enums.Provider;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserIdentityRepository extends JpaRepository<UserIdentity, Long> {
    Optional<UserIdentity> findByProviderAndProviderUserId(Provider provider, String providerUserId);

    boolean existsByUserIdAndProvider(Long id, Provider provider);
}
