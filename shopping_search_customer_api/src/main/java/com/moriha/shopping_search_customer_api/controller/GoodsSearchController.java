package com.moriha.shopping_search_customer_api.controller;

import com.moriha.common.result.BaseResult;
import com.moriha.common.service.SearchService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moriha.common.service.GoodsService;

import java.util.List;

/**
 * 商品搜索
 */
@RestController
@RequestMapping("/usr/goodsSearch")
public class GoodsSearchController {

    @DubboReference
    private SearchService searchService;

    /**
     * 自动补齐关键字
     * @param keyword
     * @return
     */
    @GetMapping("/autoSuggest")
    public BaseResult<List<String>> autoSuggest(String keyword){
        List<String> strings = searchService.autoSuggest(keyword);
        return BaseResult.ok(strings);
    }

}
