package com.mia.miamall.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.mia.miamall.manage.mapper")
public class MiaManageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiaManageServiceApplication.class, args);
    }

}
