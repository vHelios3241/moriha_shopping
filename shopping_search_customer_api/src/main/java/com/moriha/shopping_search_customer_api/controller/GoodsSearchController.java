package com.moriha.shopping_search_customer_api.controller;

import com.moriha.common.pojo.GoodsSearchParam;
import com.moriha.common.pojo.GoodsSearchResult;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.SearchService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/autoSuggest")
    public BaseResult<List<String>> autoSuggest(String keyword){
        List<String> strings = searchService.autoSuggest(keyword);
        return BaseResult.ok(strings);
    }

    /**
     * 搜索商品
     * @param goodsSearchParam 搜索条件
     * @return 搜索结果
     */
    @GetMapping("/search")
    public BaseResult<GoodsSearchResult> search(@RequestBody GoodsSearchParam goodsSearchParam){
        GoodsSearchResult result = searchService.search(goodsSearchParam);
        return BaseResult.ok(result);
    }


}
