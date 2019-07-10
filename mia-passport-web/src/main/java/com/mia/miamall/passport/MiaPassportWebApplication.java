package com.mia.miamall.passport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mia.miamall")
public class MiaPassportWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaPassportWebApplication.class, args);
    }

}
