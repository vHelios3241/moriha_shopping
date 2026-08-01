package com.moriha.common.service;

import com.moriha.common.pojo.Admin;


public interface AdminService {

    // 添加管理员
    void add(Admin admin);

    // 修改管理员
    void update(Admin admin);

    // 删除管理员
    void delete(Long id);
}
