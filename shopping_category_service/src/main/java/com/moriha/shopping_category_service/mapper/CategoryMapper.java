package com.moriha.shopping_category_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moriha.common.pojo.Category;

public interface CategoryMapper extends BaseMapper<Category> {
    // 修改广告状态
    void updateStatus(Long id, Integer status);
}
