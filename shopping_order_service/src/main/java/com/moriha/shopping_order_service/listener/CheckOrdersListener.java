package com.moriha.shopping_order_service.listener;

import com.moriha.common.pojo.Orders;
import com.moriha.common.service.OrdersService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * 延迟队列消费者，检查订单状态
 */
@Service
@RocketMQMessageListener(topic="check_orders_queue", consumerGroup="check_orders_group")
public class CheckOrdersListener implements RocketMQListener<String> {

    @Autowired
    private OrdersService ordersService;

    @Override
    public void onMessage(String orderId) {
        // 查询订单
        Orders orders = ordersService.findById(orderId);
        // 如果此时订单状态还是未支付，则将状态改为交易关闭
        if (orders.getStatus() == 1){
            orders.setStatus(6);
            ordersService.update(orders);
        }
    }
}
