package com.moriha.shopping_pay_service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@EnableDiscoveryClient
@EnableDubbo
@RefreshScope
public class ShoppingPayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingPayServiceApplication.class, args);
    }

}
