package com.moriha.shopping_admin_service.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moriha.common.pojo.Admin;
import com.moriha.common.pojo.Permission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AdminMapper extends BaseMapper<Admin> {

    // 删除管理员所有角色
    @Delete("delete from bz_admin_role where aid=#{aid}")
    void deleteAdminAllRole(Long aid);

    // 根据id查询管理员 （角色+权限）
    Admin findById(Long id);

    //给管理员添加角色
    void addRoleToAdmin(@Param("aid") Long aid, @Param("rid") Long rid);

    // 根据用户名查询管理员
    Admin findByAdminName(String username);

    // 查询管理员所有权限
    List<Permission> findAllPermission(String username);
}
