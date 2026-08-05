package com.moriha.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Admin;
import com.moriha.common.pojo.Permission;

import java.util.List;


public interface AdminService {

    // 添加管理员
    void add(Admin admin);

    // 修改管理员
    void update(Admin admin);

    // 删除管理员
    void delete(Long id);

    // 根据id查询管理员
    Admin findById(Long id);

    // 分页查询管理员
    Page<Admin> search(int page, int size);

    // 修改管理员角色
    void updateAdminRole(Long aid, Long[] rids);


    // 查询管理员所有权限
    List<Permission> findAllPermission(String username);
}
