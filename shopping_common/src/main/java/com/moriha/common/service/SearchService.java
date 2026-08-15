package com.moriha.common.service;

import com.moriha.common.pojo.GoodsDesc;
import com.moriha.common.pojo.GoodsSearchParam;
import com.moriha.common.pojo.GoodsSearchResult;

import java.util.List;

/**
 * 搜索服务
 */
public interface SearchService {

    /**
     * 自动补齐关键字
     * @param keyword 被补齐的词
     * @return 补齐的关键词集合
     */
    List<String> autoSuggest(String keyword);

    /**
     * 搜索商品
     * @param goodsSearchParam 搜索条件
     * @return 搜索结果
     */
    GoodsSearchResult search(GoodsSearchParam goodsSearchParam);

    /**
     * 向ES同步数据库中的商品数据
     * @param goodsDesc 商品详情
     */
    void syncGoodsToES(GoodsDesc goodsDesc);

    /**
     * 删除ES中的商品数据
     * @param id 商品id
     */
    void delete(Long id);

}
