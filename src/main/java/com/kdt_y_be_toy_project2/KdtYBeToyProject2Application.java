package com.kdt_y_be_toy_project2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KdtYBeToyProject2Application {

    public static void main(String[] args) {
        SpringApplication.run(KdtYBeToyProject2Application.class, args);
    }

}
