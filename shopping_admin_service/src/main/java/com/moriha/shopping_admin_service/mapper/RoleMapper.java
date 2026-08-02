package com.moriha.shopping_admin_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moriha.common.pojo.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface RoleMapper extends BaseMapper<Role> {

    // 根据id查询, 角色+权限
    Role findById(Long id);

    // 删除角色所有权限
    void deleteRoleAllPermission(Long rid);

    // 删除用户_角色表的相关数据
    void deleteRoleAllAdmin(Long id);

    // 给角色添加权限
    void addPermissionToRole(@Param("rid") Long rid, @Param("pid")Long pid);
}
