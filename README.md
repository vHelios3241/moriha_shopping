# moriha商城

一个基于 Spring Cloud Alibaba 的前后端分离微服务电商项目。

## 项目介绍
moriha商城是一款基于 Vue.js 和 Spring Cloud Alibaba 技术栈开发的电商平台，采用前后端分离架构。项目实现了商家端（权限管理、商品管理、广告管理）和用户端（用户注册与登录、商品搜索、购物车管理、下单支付、秒杀等功能）的核心功能。

## 技术栈

### 后端技术

- Java 17
- Spring Boot 3
- Spring MVC
- MyBatis-Plus
- Spring Security
- JWT


### 微服务与分布式

- Nacos（服务注册与配置中心）
- Dubbo（分布式服务调用）
- Seata（分布式事务）
- Redisson（分布式锁）
- Sentinel（流量控制与熔断保护）


### 数据存储与搜索

- MySQL 5.7
- Redis
- Elasticsearch 8
- Kibana 8


### 中间件

- RocketMQ（消息队列）
- FastDFS（分布式文件存储）


### 基础设施

- Higress（云原生网关）
- Nginx（反向代理）
- Docker（容器化部署）
- CentOS 7


### 其他

- 阿里云短信服务
- 支付宝支付
- Lombok

## 项目功能

### 用户端

- 用户注册与登录
- 商品浏览与详情查看
- 商品搜索
- 购物车管理
- 商品下单
- 在线支付
- 商品秒杀

### 商家端

- 商品管理
- 权限管理
- 广告管理

