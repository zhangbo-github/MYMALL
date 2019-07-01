package com.mia.miamall.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mia.miamall")
public class MiaItemWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaItemWebApplication.class, args);
    }

}
