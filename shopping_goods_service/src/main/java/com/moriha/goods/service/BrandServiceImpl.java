package com.moriha.goods.service;

import com.moriha.common.pojo.Brand;
import com.moriha.common.service.BrandService;
import com.moriha.goods.mapper.BrandMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@DubboService  // 开启dubbo服务
@Transactional  // 开启事务: 增删改
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @Override
    public Brand findById(Long id){
        Brand brand = brandMapper.selectById(id);
        return brand;
    }
}
