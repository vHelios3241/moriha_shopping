package com.moriha.shopping_cart_service.listener;

import com.moriha.common.pojo.CartGoods;
import com.moriha.common.service.CartService;
import lombok.AllArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * 监听同步购物车消息
 */
@Service
@RocketMQMessageListener(topic = "sync_cart_queue", consumerGroup = "sync_cart_group")
public class SyncCartListener implements RocketMQListener<CartGoods> {

    @Autowired
    private CartService cartService;

    @Override
    public void onMessage(CartGoods cartGoods) {
        cartService.refreshCartGoods(cartGoods);
    }
}
