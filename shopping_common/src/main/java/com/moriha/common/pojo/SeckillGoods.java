package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀商品
 */
@Data
public class SeckillGoods implements Serializable{
    @TableId
    private Long id; // 秒杀商品Id
    private Long goodsId; // 对应的商品Id
    private String title; // 标题
    private String introduction; // 商品描述
    private String headerPic; // 商品图片
    private BigDecimal price; // 原价
    private BigDecimal costPrice; // 秒杀价
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime; // 开始时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime; // 结束时间
    private Integer num; // 秒杀商品数
    private Integer stockCount; // 剩余库存数
}
