package com.stage.inex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InexApplication {

    public static void main(String[] args) {
        SpringApplication.run(InexApplication.class, args);
    }

}
