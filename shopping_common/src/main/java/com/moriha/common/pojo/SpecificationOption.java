package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 规格项
 */
@Data
public class SpecificationOption implements Serializable{
    @TableId
    private Long id; // 规格项id
    private String optionName; // 规格项名
    private Long specId; // 对应的规格id
}
