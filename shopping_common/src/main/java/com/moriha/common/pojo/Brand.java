package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 品牌
 */
@Data
public class Brand implements Serializable{
    @TableId
    private Long id; // 品牌 id
    private String name; // 品牌名称
}
