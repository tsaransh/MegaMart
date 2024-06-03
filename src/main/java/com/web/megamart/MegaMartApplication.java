package com.web.megamart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MegaMartApplication {
    public static void main(String[] args) {
        SpringApplication.run(MegaMartApplication.class, args);
    }
}