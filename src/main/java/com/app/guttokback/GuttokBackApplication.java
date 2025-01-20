package com.app.guttokback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GuttokBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuttokBackApplication.class, args);
    }

}
