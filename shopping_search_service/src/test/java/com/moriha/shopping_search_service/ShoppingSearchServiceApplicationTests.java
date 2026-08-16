package com.moriha.shopping_search_service;

import com.moriha.shopping_search_service.service.SearchServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ShoppingSearchServiceApplicationTests {
    @Autowired
    private SearchServiceImpl searchServiceimpl;
//    @DubboReference
//    private GoodsService goodsService;

    @Test
    void contextLoads() {
        List<String> analyze = searchServiceimpl.analyze("我爱所有人", "ik_pinyin");
        System.out.println(analyze);
    }

//    /**
//     * 同步商品数据到ES
//     */
//    @Test
//    void testSyncGoodsToES(){
//        List<GoodsDesc> goods = goodsService.findAll();
//        for (GoodsDesc goodsDesc : goods) {
//            // 如果商品是上架状态
//            if (goodsDesc.getIsMarketable()){
//                searchServiceimpl.syncGoodsToES(goodsDesc);
//            }
//        }
//    }
}
