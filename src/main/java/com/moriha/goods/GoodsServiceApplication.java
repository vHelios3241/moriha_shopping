package com.moriha.goods;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 商品服务启动类
 */
@SpringBootApplication
@EnableDubbo  // 开启dubbo功能
@EnableDiscoveryClient  // 开启服务注册与发现功能
@RefreshScope  //配置动态刷新
@MapperScan("com.moriha.goods.mapper")  // 扫描mapper，启动时加载mapper接口
public class GoodsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoodsServiceApplication.class, args);
    }
}
