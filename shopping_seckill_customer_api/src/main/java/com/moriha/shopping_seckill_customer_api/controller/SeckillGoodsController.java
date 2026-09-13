package com.moriha.shopping_seckill_customer_api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.SeckillGoods;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.SeckillService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * 秒杀商品
 */
@RestController
@RequestMapping("/user/seckillGoods")
public class SeckillGoodsController {

    @DubboReference
    private SeckillService seckillService;

    /*
     * 用户分页查询秒杀商品
     * @param page 页数
     * @param size 每页条数
     * @return 查询结果
     */
    @GetMapping("/findPage")
    public BaseResult<Page<SeckillGoods>> findPage(int page, int size){
        Page<SeckillGoods> resultPage = seckillService.findPageByRedis(page, size);
        return BaseResult.ok(resultPage);
    }
}
