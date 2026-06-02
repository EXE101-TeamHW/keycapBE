package com.keycap.keycapdesign;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
@EnableJpaAuditing
public class KeycapDesignBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeycapDesignBeApplication.class, args);
    }

    @Bean
    public CommandLineRunner alterTableRunner(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN bank_account NVARCHAR(255)");
                System.out.println("ALTER COLUMN bank_account NVARCHAR(255) successful!");
            } catch (Exception e) {
                System.out.println("ALTER COLUMN statement skipped or failed: " + e.getMessage());
            }
        };
    }
}
