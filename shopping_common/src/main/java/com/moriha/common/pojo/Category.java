package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 广告
 */
@Data
public class Category implements Serializable{
    @TableId
    private Long id; // 广告id
    private String title; // 广告标题
    private String url; // 广告链接
    private String pic; // 图片地址
    private Integer status; // 广告状态 0:未启用 1:启用
}
