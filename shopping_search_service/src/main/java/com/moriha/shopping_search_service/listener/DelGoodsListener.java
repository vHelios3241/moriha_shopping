package com.moriha.shopping_search_service.listener;

import com.moriha.common.service.SearchService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * 监听删除商品消息
 */

@Service
@RocketMQMessageListener(topic = "del_goods_queue", consumerGroup = "del_goods_group")
public class DelGoodsListener implements RocketMQListener<Long> {

    @Autowired
    private SearchService searchService;

    @Override
    public void onMessage(Long id) {
        System.out.println("删除es商品");
        searchService.delete(id);
    }

}
