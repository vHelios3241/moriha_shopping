package com.moriha.manager.controller;

import com.moriha.common.pojo.Brand;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.BrandService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @DubboReference  // Dubbo远程调用，引用goods_service提供的BrandService
    private BrandService brandService;

    @GetMapping("/findById")
    public BaseResult<Brand> findById(@RequestParam Long id) {

        Brand brand = brandService.findById(id);
        return BaseResult.ok(brand);
    }
}
