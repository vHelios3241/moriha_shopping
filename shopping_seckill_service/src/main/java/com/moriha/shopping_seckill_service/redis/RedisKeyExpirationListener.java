package com.moriha.shopping_seckill_service.redis;

import com.moriha.common.pojo.Orders;
import com.moriha.common.pojo.SeckillGoods;
import com.moriha.common.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/*
 * redis监听类 继承KeyExpirationEventMessageListener
 */
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillService seckillService;


    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    /*
     * 订单过期后，关闭交易，回退商品库存
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {

        // 获取失效key，即订单id
        String orderId = message.toString();

        // 拿到复制订单信息
        Orders orders = (Orders) redisTemplate.opsForValue().get(orderId + "_copy");
        if (orders == null || orders.getCartGoods() == null || orders.getCartGoods().isEmpty()) {
            return; // 副本已过期或数据异常，直接结束，避免空指针
        }
        Long id = orders.getCartGoods().get(0).getGoodId(); // 获取秒杀商品id
        Integer num = orders.getCartGoods().get(0).getNum(); // 获取秒杀商品数量

        // 查询秒杀商品
        SeckillGoods seckillGoods = seckillService.findSeckillGoodsByRedis(id);

        // 回退库存
        seckillGoods.setStockCount(seckillGoods.getStockCount() + num);
        redisTemplate.boundHashOps("seckillGoods").put(id, seckillGoods);

        // 删除复制订单数据
        redisTemplate.delete(orderId + "_copy");
    }
}
