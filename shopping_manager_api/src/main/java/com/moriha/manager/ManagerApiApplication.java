package com.moriha.manager;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 管理端API启动类
 */
@SpringBootApplication(exclude =  {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient  // 开启服务注册与发现功能
@RefreshScope  // 配置动态刷新
@EnableDubbo
public class ManagerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagerApiApplication.class, args);
    }
}
