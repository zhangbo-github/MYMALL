package com.mia.miamall.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.mia.miamall")
@MapperScan(basePackages = "com.mia.miamall.order.mapper")
public class MiaOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaOrderServiceApplication.class, args);
    }

}
