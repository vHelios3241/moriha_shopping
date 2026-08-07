package com.moriha.goods.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Brand;
import com.moriha.common.service.BrandService;
import com.moriha.goods.mapper.BrandMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


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

    /**
     * 查询所有品牌
     * @return
     */
    @Override
    public List<Brand> findAll() {
        List<Brand> brands = brandMapper.selectList(null);
        return brands;
    }

    /**
     * 新增品牌
     * @param brand
     */
    @Override
    public void add(Brand brand) {
        brandMapper.insert(brand);
    }

    /**
     * 修改品牌
     * @param brand
     */
    @Override
    public void update(Brand brand) {
        brandMapper.updateById(brand);
    }

    /**
     * 删除品牌
     * @param id
     */
    @Override
    public void delete(Long id) {
        brandMapper.deleteById(id);
    }

    /**
     * 分页查询品牌
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Brand> search(Brand brand, int page, int size) {
        QueryWrapper<Brand> wrapper = new QueryWrapper<>();
        // 判断品牌名不为空
        if (brand != null && StringUtils.hasText(brand.getName())) {
            wrapper.like("name", brand.getName());
        }
        Page<Brand> brandPage = brandMapper.selectPage(new Page<>(page, size), wrapper);
        return brandPage;
    }
}
