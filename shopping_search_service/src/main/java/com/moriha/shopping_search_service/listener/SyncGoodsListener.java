package com.moriha.shopping_search_service.listener;

import com.moriha.common.pojo.GoodsDesc;
import com.moriha.common.service.SearchService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * 监听同步商品消息
 */
@Service
@RocketMQMessageListener(topic = "sync_goods_queue", consumerGroup = "sync_goods_group")
public class SyncGoodsListener implements RocketMQListener<GoodsDesc> {

    @Autowired
    private SearchService searchService;

    @Override
    public void onMessage(GoodsDesc goodsDesc) {
        System.out.println("同步es商品");
        searchService.syncGoodsToES(goodsDesc);
    }
}
