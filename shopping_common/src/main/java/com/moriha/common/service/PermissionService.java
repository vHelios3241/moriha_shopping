package com.moriha.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Permission;

import java.util.List;

public interface PermissionService {
    // 新增权限
    void add(Permission permission);
    // 修改权限
    void update(Permission permission);
    // 删除权限
    void delete(Long id);
    // 根据id查询权限
    Permission findById(Long id);
    // 分页查询权限
    Page<Permission> search(int page, int size);
    // 查询所有权限
    List<Permission> findAll();
}
