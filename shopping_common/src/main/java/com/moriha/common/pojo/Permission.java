package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 后台管理员权限
 */
@Data
public class Permission implements Serializable {
    @TableId
    private Long pid; // 权限id
    private String permissionName; // 权限名
    private String url; // 权限的资源路径
}
