package com.moriha.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Brand;

import java.util.List;

/**
 * 品牌服务
 */

public interface BrandService {

    // 根据id查询品牌
    Brand findById(Long id);

    // 查询所有品牌
    List<Brand> findAll();

    // 新增品牌
    void add(Brand brand);

    // 修改品牌
    void update(Brand brand);

    // 删除品牌
    void delete(Long id);

    // 分页查询品牌
    Page<Brand> search(Brand brand, int page, int size);
}

