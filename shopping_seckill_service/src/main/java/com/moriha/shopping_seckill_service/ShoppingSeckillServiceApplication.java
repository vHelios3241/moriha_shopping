package com.moriha.shopping_seckill_service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@EnableDiscoveryClient
@EnableDubbo
@RefreshScope
@SpringBootApplication
@MapperScan("com.itbaizhan.shopping_seckill_service.mapper")
public class ShoppingSeckillServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingSeckillServiceApplication.class, args);
    }

}
