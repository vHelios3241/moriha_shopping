package com.moriha.common.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 商品搜索条件
 */
@Data
public class GoodsSearchParam implements Serializable {
    private String keyword; // 关键字
    private String brand; // 品牌名
    private Double highPrice; //最高价
    private Double lowPrice; //最低价
    private Map<String,String> specificationOption; // 规格map, 键：规格名，值：规格值
    private String sortFiled; //排序字段 NEW:新品 PRICE:价格
    private String sort; //排序方式 ASC:升序 DESC:降序
    private Integer page; //页码
    private Integer size; //每页条数
}
