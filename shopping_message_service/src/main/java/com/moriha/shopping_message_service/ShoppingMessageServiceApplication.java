package com.moriha.shopping_message_service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@EnableDiscoveryClient
@EnableDubbo
@RefreshScope
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class ShoppingMessageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingMessageServiceApplication.class, args);
    }

}
