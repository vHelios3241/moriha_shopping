package com.moriha.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Category;

import java.util.List;

/**
 * 广告服务
 */
public interface CategoryService {
    // 增加广告
    void add(Category category);
    // 修改广告
    void update(Category category);
    // 修改广告状态
    void updateStatus(Long id, Integer status);
    // 删除广告
    void delete(Long[] ids);
    // 根据Id查询广告
    Category findById(Long id);
    // 分页查询广告
    Page<Category> search(int page, int size);
    // 查询全部启用广告
    List<Category> findAll();
}
