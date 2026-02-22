package com.dean.baby.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
    scanBasePackages = {"com.dean.baby.api", "com.dean.baby"},
    exclude = {UserDetailsServiceAutoConfiguration.class}
)
@EntityScan("com.dean.baby.entity")
@EnableJpaRepositories("com.dean.baby.repository")
public class BabyDevelopmentBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BabyDevelopmentBackendApplication.class, args);
    }

}
