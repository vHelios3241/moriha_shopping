package com.moriha.shopping_seckill_customer_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Orders;
import com.moriha.common.pojo.SeckillGoods;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.SeckillService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/*
 * 秒杀商品
 */
@RestController
@RequestMapping("/user/seckillGoods")
public class SeckillGoodsController {

    @DubboReference
    private SeckillService seckillService;

    /*
     * 用户分页查询秒杀商品
     * @param page 页数
     * @param size 每页条数
     * @return 查询结果
     */
    @GetMapping("/findPage")
    public BaseResult<Page<SeckillGoods>> findPage(int page, int size){
        Page<SeckillGoods> resultPage = seckillService.findPageByRedis(page, size);
        return BaseResult.ok(resultPage);
    }

    /*
     * 用户查询秒杀商品详情
     * @param id 商品Id
     * @return 查询结果
     */
    @GetMapping("/findById")
    public BaseResult<SeckillGoods> findById(Long id){
        SeckillGoods seckillGoods = seckillService.findSeckillGoodsByRedis(id);
        return BaseResult.ok(seckillGoods);
    }

    /*
     * 生成秒杀订单
     * @param orders 订单对象
     * @return 生成的订单
     */
    @PostMapping("/add")
    public BaseResult<Orders> add(@RequestBody Orders orders, @RequestHeader Long userId){
        orders.setUserId(userId);
        Orders order = seckillService.createOrder(orders);
        return BaseResult.ok(order);
    }

}
