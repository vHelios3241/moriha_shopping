package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 省份
 */
@Data
public class Province implements Serializable{
    @TableId
    private String id; // 省份id
    private String provinceName; // 省份名
}
