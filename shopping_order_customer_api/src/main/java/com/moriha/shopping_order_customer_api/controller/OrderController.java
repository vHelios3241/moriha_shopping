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

    /*
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

    /*
     * 查询用户的订单
     * @param status 订单状态：1.未付款 2.已付款 3.未发货 4.已发货 5.交易成功 6.交易关闭 7.待评价，传入空值代表查询所有
     * @param userId 用户id
     * @return 查询结果
     */
    @GetMapping("/findUserOrders")
    public BaseResult<List<Orders>> findUserOrders( Long userId,  Integer status){
        return BaseResult.ok(ordersService.findUserOrders(userId, status));
    }
    /*
     * 查询订单详情
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public BaseResult<Orders> findById(String id){
        return BaseResult.ok(ordersService.findById(id));
    }

}
