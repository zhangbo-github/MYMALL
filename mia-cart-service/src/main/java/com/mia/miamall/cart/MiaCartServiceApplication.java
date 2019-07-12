package com.mia.miamall.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mia.miamall")
@MapperScan(basePackages = "com.mia.miamall.cart.mapper")
public class MiaCartServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaCartServiceApplication.class, args);
    }

}
