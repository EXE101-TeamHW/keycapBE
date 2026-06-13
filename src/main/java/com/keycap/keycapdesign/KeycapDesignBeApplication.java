package com.keycap.keycapdesign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KeycapDesignBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeycapDesignBeApplication.class, args);
    }
}
