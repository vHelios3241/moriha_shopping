package com.moriha.goods.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.ProductType;
import com.moriha.common.result.BusException;
import com.moriha.common.result.CodeEnum;
import com.moriha.common.service.ProductTypeService;
import com.moriha.goods.mapper.ProductTypeMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@DubboService
@Transactional
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    private ProductTypeMapper productTypeMapper;

    /**
     * 新增商品类型
     * @param productType
     */
    @Override
    public void add(ProductType productType) {

        // 根据父类型id查询父类型
        ProductType productTypeParent  = productTypeMapper.selectById(productType.getParentId());

        // 根据父类型的级别，来设置当前类型的级别
        if(productTypeParent == null){
            // 如果没有父类型，则为1级类型
            productType.setLevel(1);

        }else if(productTypeParent.getLevel()<3){
            // 如果父类型级<3 则级别为父级别+1
            productType.setLevel(productTypeParent.getLevel() + 1);

        }else if(productTypeParent.getLevel()==3){
            // 如果父类型级=3 则不能添加子级别
            throw new BusException(CodeEnum.INSERT_PRODUCT_TYPE_ERROR);
        }

        productTypeMapper.insert(productType);
    }

    /**
     * 修改商品类型
     * @param productType
     */
    @Override
    public void update(ProductType productType) {

        ProductType productTypeParent  = productTypeMapper.selectById(productType.getParentId());

        if(productTypeParent == null){
            productType.setLevel(1);
        }else if(productTypeParent.getLevel()<3){
            productType.setLevel(productTypeParent.getLevel() + 1);
        }else if(productTypeParent.getLevel()==3){
            throw new BusException(CodeEnum.INSERT_PRODUCT_TYPE_ERROR);
        }

        productTypeMapper.updateById(productType);
    }

    /**
     * 删除商品类型
     * @param id
     */
    @Override
    public void delete(Long id) {

        // 查询该类型的子类型
        QueryWrapper<ProductType> productTypeQueryWrapper = new QueryWrapper<>();
        productTypeQueryWrapper.eq("parentId", id);
        List<ProductType> productTypes = productTypeMapper.selectList(productTypeQueryWrapper);

        // 如果该类型有子类型，删除失败
        if(!CollectionUtils.isEmpty(productTypes)){
            throw new BusException(CodeEnum.DELETE_PRODUCT_TYPE_ERROR);
        }

        productTypeMapper.deleteById(id);
    }

    /**
     * 根据id查询商品类型
     * @param id
     * @return
     */
    @Override
    public ProductType findById(Long id) {
        return productTypeMapper.selectById(id);
    }

    /**
     * 分页查询
     * @param productType
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<ProductType> search(ProductType productType, int page, int size) {
        QueryWrapper<ProductType> productTypeQueryWrapper = new QueryWrapper<>();
        if(productType != null){
            // 类型名不为空时
            if(StringUtils.hasText(productType.getName())){
                productTypeQueryWrapper.like("name", productType.getName());
            }
            // 上级类型id不为空时
            if(productType.getParentId() != null){
                productTypeQueryWrapper.eq("parentId", productType.getParentId());
            }
        }

        return productTypeMapper.selectPage(new Page<>(page,size), productTypeQueryWrapper);
    }

    /**
     * 根据条件查询商品类型
     * @param productType
     * @return
     */
    @Override
    public List<ProductType> findProductType(ProductType productType) {

        QueryWrapper<ProductType> productTypeQueryWrapper = new QueryWrapper<>();
        if(productType != null){

            if(StringUtils.hasText(productType.getName())){
                productTypeQueryWrapper.like("name", productType.getName());
            }
            if(productType.getParentId() != null){
                productTypeQueryWrapper.eq("parentId", productType.getParentId());
            }
        }
        return productTypeMapper.selectList(productTypeQueryWrapper);
    }
}
