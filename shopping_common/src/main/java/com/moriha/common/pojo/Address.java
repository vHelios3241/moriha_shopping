package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 收货地址
 */
@Data
public class Address implements Serializable {
    @TableId
    private Long id; // 地址id
    private Long userId; // 用户id
    private String provinceName; // 省份名
    private String cityName; // 市名
    private String areaName; // 县/区名
    private String address; // 详细地址
    private String mobile; // 手机
    private String contact; // 联系人姓名
    private String zipCode; // 邮编
}
