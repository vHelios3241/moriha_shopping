package com.moriha.shopping_admin_service.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moriha.common.pojo.Admin;
import org.apache.ibatis.annotations.Delete;

public interface AdminMapper extends BaseMapper<Admin> {

    // 删除用户角色
    @Delete("delete from bz_admin_role where aid=#{aid}")
    void deleteAdminRole(Long aid);
}
