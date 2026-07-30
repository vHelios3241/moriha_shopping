package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品详情
 */
@Data
public class GoodsDesc implements Serializable {
    private Long id; // 商品id
    private String goodsName; // 商品名称
    private String caption; // 副标题
    private BigDecimal price; // 价格
    private String headerPic; // 头图
    private Boolean isMarketable; // 是否上架
    private String introduction; // 商品介绍

    private Brand brand; // 品牌
    private ProductType productType1; // 一级类目
    private ProductType productType2;  // 二级类目
    private ProductType productType3;  // 三级类目
    private List<GoodsImage> images; // 商品图片
    private List<Specification> specifications; // 商品规格
}
