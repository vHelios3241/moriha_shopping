package com.moriha.manager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.ProductType;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.ProductTypeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品类型
 */
@RestController
@RequestMapping("/productType")
public class ProductTypeController {

    @DubboReference
    private ProductTypeService productTypeService;

    /**
     * 新增商品类型
     * @param productType 商品类型
     * @return 执行结果
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody ProductType productType){
        productTypeService.add(productType);
        return BaseResult.ok();
    }

    /**
     * 修改商品类型
     * @param productType 商品类型
     * @return 执行结果
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody ProductType productType){
        productTypeService.update(productType);
        return BaseResult.ok();
    }

    /**
     * 删除商品类型
     * @param id 商品类型id
     * @return 执行结果
     */
    @DeleteMapping("/delete")
    public BaseResult delete(Long id){
        productTypeService.delete(id);
        return BaseResult.ok();
    }

    /**
     * 根据id查询商品类型
     * @param id 商品类型id
     * @return 查询结果
     */
    @GetMapping("/findById")
    public BaseResult<ProductType> findById(Long id){
        ProductType productType = productTypeService.findById(id);
        return BaseResult.ok(productType);
    }

    /**
     * 分页查询商品类型
     * @param productType 查询条件对象
     * @param page 页码
     * @param size 每页条数
     * @return 查询结果
     */
    @GetMapping("/search")
    public BaseResult<Page<ProductType>> search(ProductType productType, int page, int size){
        Page<ProductType> page1 = productTypeService.search(productType, page, size);
        return BaseResult.ok(page1);
    }

    /**
     * 查询商品类型列表
     * @param productType 查询条件对象
     * @return 查询结果
     */
    @GetMapping("/findProductType")
    public BaseResult<List<ProductType>> findProductType(ProductType productType){
        List<ProductType> productType1 = productTypeService.findProductType(productType);
        return BaseResult.ok(productType1);
    }

    /**
     * 根据父类型id查询商品类型列表
     * @param parentId 父类型id
     * @return 查询结果
     */
    @GetMapping("/findByParentId")
    public BaseResult<List<ProductType>> findByParentId(Long parentId){
        ProductType productType = new ProductType();
        productType.setParentId(parentId);
        List<ProductType> productType1 = productTypeService.findProductType(productType);
        return BaseResult.ok(productType1);
    }
}

