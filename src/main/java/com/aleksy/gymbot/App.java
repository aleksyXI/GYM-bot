package com.aleksy.gymbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

// Аннотация, которая объединяет в себя @Configuration, @EnableAutoConfiguration, @ComponentScan
@SpringBootApplication
public class App {
    public static void main(String[] args) {

        ApiContextInitializer.init();

        SpringApplication.run(App.class);
    }
}