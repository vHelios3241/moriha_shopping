package com.moriha.shopping_order_customer_api.controller;

import com.moriha.common.pojo.CartGoods;
import com.moriha.common.pojo.Orders;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.CartService;
import com.moriha.common.service.OrdersService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user/order")
@RestController
public class OrderController {

    @DubboReference
    private OrdersService ordersService;
    @DubboReference
    private CartService cartService;

    /**
     * 添加订单
     * @param orders
     * @param userId
     * @return
     */
    @PostMapping("/add")
    public BaseResult<Orders> add(@RequestBody Orders orders, @RequestHeader Long userId){
        // 保存订单
        orders.setUserId(userId);
        Orders add = ordersService.add(orders);
        // 将redis的购物车数据删除
        List<CartGoods> cartGoods = orders.getCartGoods();
        for (CartGoods cartGood : cartGoods) {
            cartService.deleteCartOption(userId, cartGood.getGoodId());
        }
        return BaseResult.ok(add);
    }
}
