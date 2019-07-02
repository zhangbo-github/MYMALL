package com.mia.miamall.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.mia.miamall.manage.mapper")
@ComponentScan(basePackages = "com.mia.miamall")
public class MiaManageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaManageServiceApplication.class, args);
    }

}
