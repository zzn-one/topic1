package com.zzn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = "com.zzn.mapper")
@EnableScheduling
public class Topic1Application {

    public static void main(String[] args) {
        SpringApplication.run(Topic1Application.class, args);
    }


}
