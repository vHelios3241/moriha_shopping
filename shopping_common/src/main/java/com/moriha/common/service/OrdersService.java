package com.moriha.common.service;

import com.moriha.common.pojo.Orders;
import java.util.List;

public interface OrdersService {
    // 生成订单
    Orders add(Orders orders);
    // 修改订单
    void update(Orders orders);
    // 根据id查询订单
    Orders findById(String id);
    // 查询所有订单
    List<Orders> findUserOrders(Long userId, Integer status);
}
