package com.moriha.manager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Permission;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.PermissionService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @DubboReference
    private PermissionService permissionService;

    /**
     * 新增权限
     * @param permission
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody Permission permission) {
        permissionService.add(permission);
        return BaseResult.ok();
    }

    /**
     * 修改权限
     * @param permission
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody Permission permission) {
        permissionService.update(permission);
        return BaseResult.ok();
    }

    /**
     * 删除权限
     * @param pid
     */
    @DeleteMapping("/delete")
    public BaseResult delete(Long pid) {
        permissionService.delete(pid);
        return BaseResult.ok();
    }

    /**
     * 根据id查询权限
     * @param pid
     * @return
     */
    @GetMapping("/findById")
    public BaseResult<Permission> findById(Long pid) {
        Permission byId = permissionService.findById(pid);
        return BaseResult.ok(byId);
    }

    /**
     * 分页查询权限
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public BaseResult<Page<Permission>> search(int page, int size) {
        Page<Permission> search = permissionService.search(page, size);
        return BaseResult.ok(search);
    }

    /**
     * 查询所有权限
     * @return
     */
    @GetMapping("/findAll")
    public BaseResult<List<Permission>> findAll() {
        List<Permission> all = permissionService.findAll();
        return BaseResult.ok(all);
    }


}
