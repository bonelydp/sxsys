package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@MapperScan(basePackages = "com.example.mapper")
@EnableJpaRepositories(basePackages = "com.example.Repository")
public class ShouyiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShouyiApplication.class, args);
    }
}
