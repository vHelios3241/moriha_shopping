package com.moriha.shopping_cart_customer_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@RefreshScope
public class ShoppingCartCustomerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartCustomerApiApplication.class, args);
    }

}
