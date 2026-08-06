package com.moriha.manager.security;

import com.moriha.common.pojo.Admin;
import com.moriha.common.pojo.Permission;
import com.moriha.common.service.AdminService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// 认证授权逻辑
@Service
public class MyUserDetailService implements UserDetailsService{

    @DubboReference
    private AdminService adminService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. 认证
        Admin admin = adminService.findByAdminName(username);
        if(admin == null){
            throw new UsernameNotFoundException("用户名不存在");
        }

        // 2. 授权 (查询出所有权限 然后选择赋予）
        List<Permission> permissions = adminService.findAllPermission(username);
        List<GrantedAuthority> grantedAuthority = new ArrayList<>();
        if (permissions.get(0) != null){
            for (Permission permission : permissions) {
                grantedAuthority.add(new SimpleGrantedAuthority(permission.getUrl()));
            }
        }



        // 3. 封装为UserDetails对象
        UserDetails userDetails = User.withUsername(admin.getUsername())
                .password(admin.getPassword())
                .authorities(grantedAuthority)
                .build();
        // 4. 返回封装好的UserDetails对象
        return userDetails;

    }
}
