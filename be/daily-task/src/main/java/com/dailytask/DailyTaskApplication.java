package com.dailytask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class DailyTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(DailyTaskApplication.class, args);
    }

}
