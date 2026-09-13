package com.moriha.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Orders;
import com.moriha.common.pojo.SeckillGoods;

public interface SeckillService {

    // 前台用户查询秒杀商品
    Page<SeckillGoods> findPageByRedis(int page, int size);
    // 查询秒杀商品详情
    SeckillGoods findSeckillGoodsByRedis(Long goodsId);
    // 生成秒杀订单
    Orders createOrder(Orders orders);
    // 根据id查询秒杀订单
    Orders findOrder(String id);
    //支付秒杀订单
    Orders pay(String orderId);

}
