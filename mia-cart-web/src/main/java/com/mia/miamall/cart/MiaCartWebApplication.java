package com.mia.miamall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mia.miamall")
public class MiaCartWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaCartWebApplication.class, args);
    }

}
