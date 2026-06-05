package com.keycap.keycapdesign;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

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
            try {
                List<Map<String, Object>> constraints = jdbcTemplate.queryForList("""
                        SELECT cc.name
                        FROM sys.check_constraints cc
                        JOIN sys.tables t ON cc.parent_object_id = t.object_id
                        WHERE t.name = 'orders'
                          AND cc.definition LIKE '%payment_status%'
                        """);
                for (Map<String, Object> row : constraints) {
                    String constraintName = String.valueOf(row.get("name")).replace("]", "]]");
                    jdbcTemplate.execute("ALTER TABLE orders DROP CONSTRAINT [" + constraintName + "]");
                }
                jdbcTemplate.execute("""
                        ALTER TABLE orders ADD CONSTRAINT CK_orders_payment_status
                        CHECK (payment_status IN ('PENDING', 'PAID', 'REFUNDED', 'CANCELLED'))
                        """);
                System.out.println("ALTER CHECK payment_status successful!");
            } catch (Exception e) {
                System.out.println("ALTER CHECK payment_status skipped or failed: " + e.getMessage());
            }
        };
    }
}
