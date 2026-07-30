package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 区/县
 */
@Data
public class Area implements Serializable{
    @TableId
    private String id; // 区/县Id
    private String area; // 区/县名
    private String cityId; // 城市Id

}
