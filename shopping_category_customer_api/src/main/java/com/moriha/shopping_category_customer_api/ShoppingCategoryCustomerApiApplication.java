package com.moriha.shopping_category_customer_api;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 广告
 */
@SpringBootApplication(exclude =  {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient //向注册中心注册该服务
@RefreshScope // 配置动态刷新
public class ShoppingCategoryCustomerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCategoryCustomerApiApplication.class, args);
    }

}
