package com.moriha.manager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Role;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.RoleService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/role")
public class RoleServiceController {

    @DubboReference
    private RoleService roleService;

    /**
     * 新增角色
     * @param role
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody Role role) {
        roleService.add(role);
        return BaseResult.ok();
    }

    /**
     * 修改角色
     * @param role
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody Role role) {
        roleService.update(role);
        return BaseResult.ok();
    }

    /**
     * 删除角色
     * @param id
     */
    @DeleteMapping("/delete")
    public BaseResult delete(Long id) {
        roleService.delete(id);
        return BaseResult.ok();
    }

    /**
     * 根据id查询角色
     * @param rid
     * @return
     */
    @GetMapping("/findById")
    public BaseResult<Role> findById(Long rid) {
        Role byId = roleService.findById(rid);
        return BaseResult.ok(byId);
    }

    /**
     * 查询所有角色
     * @return
     */
    @GetMapping("/findAll")
    public BaseResult<List<Role>> findAll() {
        List<Role> all = roleService.findAll();
        return BaseResult.ok(all);
    }

    /**
     * 分页查询角色
     * @param page 当前页码
     * @param size 每页显示条数
     * @return 返回分页查询结果，包含角色数据
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('/role/search')")
    public BaseResult<Page<Role>> search(int page, int size) {
        Page<Role> search = roleService.search(page, size);
        return BaseResult.ok(search);
    }

    /**
     * 修改角色权限
     * @param rid
     * @param pids
     */
    @PutMapping("/updatePermissionToRole")
    public BaseResult updatePermissionToRole(Long rid, Long[] pids) {
        roleService.updatePermissionToRole(rid, pids);
        return BaseResult.ok();
    }
}
