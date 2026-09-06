package com.moriha.shopping_order_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moriha.common.pojo.Orders;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/* 订单Mapper */
public interface OrdersMapper extends BaseMapper<Orders> {
    // 查询订单详情
    Orders findById(String id);
    // 查询用户所有订单
    List<Orders> findUserOrders(@Param("userId") Long userId, @Param("status") Integer status);
}
