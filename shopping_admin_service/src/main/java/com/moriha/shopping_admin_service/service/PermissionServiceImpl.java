package com.moriha.shopping_admin_service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Permission;
import com.moriha.common.service.PermissionService;
import com.moriha.shopping_admin_service.mapper.PermissionMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 新增权限
     * @param permission
     */
    @Override
    public void add(Permission permission) {
        permissionMapper.insert(permission);
    }

    /**
     * 修改权限
     * @param permission
     */
    @Override
    public void update(Permission permission) {
        permissionMapper.updateById(permission);
    }

    /**
     * 删除权限
     * @param pid
     */
    @Override
    public void delete(Long pid) {
        // 删除权限
        permissionMapper.deleteById(pid);
        // 删除角色_权限表中的相关数据
        permissionMapper.deletePermissionAllRole(pid);
    }

    /**
     * 根据id查询权限
     * @param pid
     * @return
     */
    @Override
    public Permission findById(Long pid) {
        return permissionMapper.selectById(pid);
    }

    /**
     * 分页查询权限
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Permission> search(int page, int size) {
        return permissionMapper.selectPage(new Page<>(page, size), null);
    }

    /**
     * 查询所有权限
     *
     * @return
     */
    @Override
    public List<Permission> findAll() {
        return permissionMapper.selectList(null);
    }

}
