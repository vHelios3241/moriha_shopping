package com.moriha.shopping_custcare_service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

@EnableDiscoveryClient
@RefreshScope
@EnableDubbo
@MapperScan("com.moriha.shopping_custcare_service.mapper")
@SpringBootApplication
public class ShoppingCustcareServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCustcareServiceApplication.class, args);
    }

    /*
     * AI聊天客户端
     * @param builder
     * @return
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder){
        return builder
                .defaultFunctions("getOrderQueryService")
                .build();
    }

}
