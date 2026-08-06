package com.moriha.manager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Admin;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.AdminService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.Security;

@RestController
@RequestMapping("/admin")
public class AdminServiceController {

    @DubboReference(check = false)
    private AdminService adminService;
    @Autowired
    private PasswordEncoder encoder;

    /**
     * 添加管理员
     * @param admin
     * @return
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody Admin admin){
        String password = admin.getPassword();
        password = encoder.encode(password);
        admin.setPassword(password);
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
        String password = admin.getPassword();
        if(StringUtils.hasText(password)){
            // 密码不为空加密
            password = encoder.encode(password);
            admin.setPassword(password);
        }
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

    /**
     * 根据id查询管理员
     * @param aid
     * @return
     */
    @GetMapping("/findById")
    public BaseResult<Admin> findById(@RequestParam Long aid){
        Admin byId = adminService.findById(aid);
        return BaseResult.ok(byId);
    }

    /**
     * 分页查询管理员
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/search")
    public BaseResult<Page<Admin>> search(int page, int size){
        Page<Admin> search = adminService.search(page, size);
        return BaseResult.ok(search);
    }

    /**
     * 修改管理员角色
     * @param aid
     * @param rids
     */
    @PutMapping("/updateRoleToAdmin")
    public BaseResult updateRoleToAdmin(Long aid, Long[] rids){
        adminService.updateAdminRole(aid, rids);
        return BaseResult.ok();
    }

    /**
     * 获取登录管理员名
     * @return 管理员名
     */
    @GetMapping("/getUsername")
    public BaseResult<String> getUsername() {
        // 1.获取会话对象
        SecurityContext context = SecurityContextHolder.getContext();
        // 2.获取认证对象
        Authentication authentication = context.getAuthentication();
        // 3.获取登录用户信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return BaseResult.ok(username);

    }
}
