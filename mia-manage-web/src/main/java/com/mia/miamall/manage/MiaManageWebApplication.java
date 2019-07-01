package com.mia.miamall.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mia.miamall")
public class MiaManageWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaManageWebApplication.class, args);
    }

}
