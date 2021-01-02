package ru.petrukhin.alexgif;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AlexgifApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlexgifApplication.class, args);
    }

}
