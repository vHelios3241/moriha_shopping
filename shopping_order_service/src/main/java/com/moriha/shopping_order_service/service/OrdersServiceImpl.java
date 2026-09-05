package com.moriha.shopping_order_service.service;

import com.moriha.common.pojo.CartGoods;
import com.moriha.common.pojo.Orders;
import com.moriha.common.service.OrdersService;

import com.moriha.shopping_order_service.mapper.CartGoodsMapper;
import com.moriha.shopping_order_service.mapper.OrdersMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@DubboService
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private CartGoodsMapper cartGoodsMapper;

    /*
     * 生成订单
     */
    @Override
    public Orders add(Orders orders) {
        // 设置订单状态未付款
        orders.setStatus(1);
        // 设置订单创建时间
        orders.setCreateTime(new Date());
        // 计算订单价格，遍历订单所有商品
        List<CartGoods> cartGoodsList = orders.getCartGoods();
        BigDecimal sum = BigDecimal.ZERO;
        for (CartGoods cartGood : cartGoodsList) {
            // 数量
            BigDecimal num = BigDecimal.valueOf(cartGood.getNum());
            // 单价
            BigDecimal price = cartGood.getPrice();
            // 数量*单价
            BigDecimal multiply = num.multiply(price);
            sum = sum.add(multiply);
        }
        // 保存订单
        ordersMapper.insert(orders);
        // 保存订单商品
        for (CartGoods cartGood : cartGoodsList) {
        // 购物车商品保存到数据库中
            cartGood.setOrderId(orders.getId());
            cartGoodsMapper.insert(cartGood);
        }
        return orders;
    }

    @Override
    public void update(Orders orders) {

    }

    @Override
    public Orders findById(String id) {
        return null;
    }

    @Override
    public List<Orders> findUserOrders(Long userId, Integer status) {
        return List.of();
    }
}
