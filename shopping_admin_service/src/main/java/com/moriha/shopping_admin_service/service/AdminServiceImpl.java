package com.moriha.shopping_admin_service.service;

import com.moriha.common.pojo.Admin;
import com.moriha.common.service.AdminService;
import com.moriha.shopping_admin_service.mapper.AdminMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@DubboService
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    /**
     * 添加管理员
     * @param admin
     */
    public void add(Admin admin){
        adminMapper.insert(admin);
    }

    /**
     * 修改管理员
     * @param admin
     */
    @Override
    public void update(Admin admin){
        adminMapper.updateById(admin);
    }

    /**
     * 删除管理员
     * @param id
     */
    @Override
    public void delete(Long id) {
        // 删除用户角色
        adminMapper.deleteAdminRole(id);
        //删除用户
        adminMapper.deleteById(id);
    }

    /**
     * 根据id查询管理员
     * @param id
     * @return
     */
    @Override
    public Admin findById(Long id) {
        return adminMapper.findById(id);
    }

}
