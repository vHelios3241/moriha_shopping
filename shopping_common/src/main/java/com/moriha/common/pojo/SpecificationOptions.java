package com.moriha.common.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品规格项集合，用于新增规格项
 */
@Data
public class SpecificationOptions implements Serializable{
    private Long specId; // 规格id
    private String[] optionName; // 规格项名数组
}
