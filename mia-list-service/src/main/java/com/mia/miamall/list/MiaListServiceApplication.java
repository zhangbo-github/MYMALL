package com.mia.miamall.list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mia.miamall")
public class MiaListServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaListServiceApplication.class, args);
    }



}
