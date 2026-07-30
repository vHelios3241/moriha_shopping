package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 后台管理员用户
 */
@Data
public class Admin implements Serializable {
    @TableId
    private Long aid; // 用户id
    private String username; // 用户名
    private String password; // 密码
    @TableField(exist = false) // 数据库不存在该字段
    private List<Role> roles; // 用户拥有的角色集合
}
