package com.moriha.shopping_category_customer_api.controller;

import com.moriha.common.pojo.Category;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.CategoryService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user/category")
public class CategoryController {

    @DubboReference
    private CategoryService categoryService;

    /**
     * 查询全部启用广告
     * @return 查询结果
     */
    @GetMapping("/all")
    public BaseResult<List<Category>> findAll(){
        List<Category> categories = categoryService.findAll();
        return BaseResult.ok(categories);
    }
}
