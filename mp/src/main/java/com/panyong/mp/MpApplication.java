package com.panyong.mp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.panyong.mp.mapper")
public class MpApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(MpApplication.class, args);
    }
    
}
