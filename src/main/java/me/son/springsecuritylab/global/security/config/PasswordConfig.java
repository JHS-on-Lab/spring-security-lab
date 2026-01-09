package me.son.springsecuritylab.global.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {
    // Security Config 에 있던 메서드를 분리한 이유?
    // OAuth2UserFactory 참조 -> OAuth2LoginSuccessHandler 참조 -> Security Config 참조 시, 순환 참조 발생
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
