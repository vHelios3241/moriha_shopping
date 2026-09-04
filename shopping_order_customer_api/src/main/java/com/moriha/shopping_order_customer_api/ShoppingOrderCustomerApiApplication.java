package com.moriha.shopping_order_customer_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@EnableDiscoveryClient
@RefreshScope
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class ShoppingOrderCustomerApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShoppingOrderCustomerApiApplication.class, args);
    }

}
