package com.mia.miamall.usermanage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.mia.miamall.usermanage.mapper")
public class MiaUsermanageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaUsermanageApplication.class, args);
    }

}
