package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品
 */
@Data
public class Goods implements Serializable {
    @TableId
    private Long id; // 商品id
    private String goodsName; // 商品名称
    private String caption; // 副标题
    private BigDecimal price; // 价格
    private Long brandId; // 品牌id
    private Long productType1Id; // 一级类目id
    private Long productType2Id; // 二级类目id
    private Long productType3Id; // 三级类目id
    private String headerPic; // 头图
    private Boolean isMarketable; // 是否上架
    private String introduction; // 商品介绍
    @TableField(exist = false)
    private List<GoodsImage> images; // 商品图片
    @TableField(exist = false)
    private List<Specification> specifications; // 商品规格

}
