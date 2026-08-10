package com.moriha.goods.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Specification;
import com.moriha.common.pojo.SpecificationOption;
import com.moriha.common.pojo.SpecificationOptions;
import com.moriha.common.service.SpecificationService;
import com.moriha.goods.mapper.SpecificationMapper;

import com.moriha.goods.mapper.SpecificationOptionMapper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@DubboService
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;
    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    /**
     * 新增商品规格
     * @param specification
     */
    @Override
    public void add(Specification specification) {
        specificationMapper.insert(specification);
    }

    /**
     * 修改商品规格
     * @param specification
     */
    @Override
    public void update(Specification specification) {
        specificationMapper.updateById(specification);
    }

    /**
     * 删除商品规格
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {   // 需要删除两张表的数据
        for (Long id : ids) {
            // 删除商品规格项
            QueryWrapper<SpecificationOption> queryWrapper = new QueryWrapper();
            queryWrapper.eq("specId",id);
            specificationOptionMapper.delete(queryWrapper);
            // 删除商品规格
            specificationMapper.deleteById(id);
        }

    }

    /**
     * 根据id查询商品规格
     * @param id
     * @return
     */
    @Override
    public Specification findById(Long id) {
        return specificationMapper.findById(id);
    }

    /**
     * 分页查询商品规格
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Specification> search(int page, int size) {
        return specificationMapper.selectPage(new Page<>(page, size),null);
    }

    /**
     * 根据商品类型id查询某种商品类型下的所有规格
     * @param id
     * @return
     */
    @Override
    public List<Specification> findByProductTypeId(Long id) {
        return specificationMapper.findByProductTypeId(id);
    }


    /**
     * 新增商品规格项
     * @param specificationOptions
     */
    @Override
    public void addOption(SpecificationOptions specificationOptions) {
        // 拿到规格项名数组
        String[] optionNames = specificationOptions.getOptionName();
        // 拿到规格id
        Long specId = specificationOptions.getSpecId();

        for (String optionName : optionNames) {
            // 构建规格项对象
            SpecificationOption specificationOptions1 = new SpecificationOption();
            specificationOptions1.setSpecId(specId);
            specificationOptions1.setOptionName(optionName);
            // 存到数据库
            specificationOptionMapper.insert(specificationOptions1);
        }
    }

    /**
     * 删除商品规格项
     * @param ids
     */
    @Override
    public void deleteOption(Long[] ids) {
        specificationOptionMapper.deleteBatchIds(Arrays.asList(ids));
    }
}
