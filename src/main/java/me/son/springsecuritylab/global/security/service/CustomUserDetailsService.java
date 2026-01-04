package me.son.springsecuritylab.global.security.service;

import lombok.RequiredArgsConstructor;

import me.son.springsecuritylab.global.security.CustomUserDetails;
import me.son.springsecuritylab.user.domain.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
                .map(u -> CustomUserDetails.builder()
                        .username(u.getUsername())
                        .password(u.getPassword())
                        .role(u.getRole())
                        .provider(u.getProvider())
                        .email(u.getEmail())
                        .build()
                ).orElseThrow(() -> new UsernameNotFoundException("User not found. " + username))
                ;
    }
}
