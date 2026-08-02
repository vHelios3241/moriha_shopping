package com.moriha.common.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Role;

import java.util.List;

public interface RoleService {

    // 新增角色
    void add(Role role);
    // 修改角色
    void update(Role role);
    // 删除角色
    void delete(Long id);
    // 根据id查询角色
    Role findById(Long id);
    // 查询所有角色
    List<Role> findAll();
    // 分页查询角色
    Page<Role> search(int page, int size);
    // 修改角色权限
    void updatePermissionToRole(Long rid, Long[] pids);
}
