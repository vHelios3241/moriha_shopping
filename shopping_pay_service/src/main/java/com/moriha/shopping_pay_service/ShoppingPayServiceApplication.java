package com.moriha.shopping_pay_service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
@RefreshScope
@MapperScan("com.moriha.shopping_pay_service.mapper")
public class ShoppingPayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingPayServiceApplication.class, args);
    }

}
