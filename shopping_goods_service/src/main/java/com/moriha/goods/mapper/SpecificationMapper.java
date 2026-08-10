package com.moriha.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.moriha.common.pojo.Specification;

import java.util.List;

public interface SpecificationMapper extends BaseMapper<Specification> {
    // 根据id查询商品规格
    Specification findById (Long productTypeId);
    // 根据商品类型查询商品规格
    List<Specification> findByProductTypeId (Long productTypeId);
}
