package com.mia.miamall.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mia.miamall")
public class MiaOrderWedApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaOrderWedApplication.class, args);
    }

}
