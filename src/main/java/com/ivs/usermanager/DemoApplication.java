package com.ivs.usermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
/* Scan all entities in the common package */
@EntityScan("com.ivs.usermanager")
/*
 * Scan all repositories under the modules package or just use the root package
 */
@EnableJpaRepositories(basePackages = "com.ivs.usermanager")
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}