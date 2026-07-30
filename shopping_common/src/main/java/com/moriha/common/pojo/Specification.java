package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 规格
 */
@Data
public class Specification implements Serializable{
    @TableId
    private Long id; // 规格id
    private String specName; // 规格名
    private Long productTypeId; //商品类型id
    @TableField(exist = false)
    private List<SpecificationOption> specificationOptions; // 规格项
}
