package com.moriha.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moriha.common.pojo.Goods;
import com.moriha.common.pojo.GoodsDesc;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper extends BaseMapper<Goods> {
    // 删除商品下的所有规格项
    void deleteGoodsSpecificationOption(Long gid);
    // 添加商品_规格项数据
    void addGoodsSpecificationOption(@Param("gid") Long gid, @Param("optionId") Long optionId);
    // 商品上下架
    void putAway(@Param("id") Long id, @Param("isMarketable") Boolean isMarketable);
    // 根据id查询商品
    Goods findById(Long id);
    // 查询所有商品详情
    List<GoodsDesc> findAll();

}

