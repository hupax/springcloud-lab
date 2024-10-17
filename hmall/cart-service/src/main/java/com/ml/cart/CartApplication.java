package com.ml.cart;


import com.ml.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.client.RestTemplate;


@EnableFeignClients(basePackages = {"com.ml.api.client"}/*, defaultConfiguration = DefaultFeignConfig.class*/)
@MapperScan("com.ml.cart.mapper")
@SpringBootApplication
public class CartApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartApplication.class, args);
    }
    
    /**
     * 向item-service 发起请求
     */
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
}
