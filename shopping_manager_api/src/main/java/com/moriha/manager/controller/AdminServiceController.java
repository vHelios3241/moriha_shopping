package com.moriha.manager.controller;

import com.moriha.common.pojo.Admin;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.AdminService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminServiceController {
    @DubboReference(check = false)
    private AdminService adminService;

    /**
     * 添加管理员
     * @param admin
     * @return
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody Admin admin){
        adminService.add(admin);
        return BaseResult.ok();
    }

    /**
     * 修改管理员
     * @param admin
     * @return
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody Admin admin){
        adminService.update(admin);
        return BaseResult.ok();
    }

    /**
     * 删除管理员
     * @param aid
     */
    @DeleteMapping("/delete")
    public BaseResult delete(@RequestParam Long aid){
        adminService.delete(aid);
        return BaseResult.ok();
    }
}
