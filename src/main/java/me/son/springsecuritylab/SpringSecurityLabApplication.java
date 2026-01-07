package me.son.springsecuritylab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringSecurityLabApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityLabApplication.class, args);
    }

}
