package com.moriha.shopping_webhooks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@RefreshScope
@EnableDiscoveryClient
public class ShoppingWebhooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingWebhooksApplication.class, args);
    }

}
