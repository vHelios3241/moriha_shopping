package com.moriha.manager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.SeckillGoods;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.SeckillGoodsService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/**
 * 秒杀商品
 */
@RestController
@RequestMapping("/seckillGoods")
public class SeckillGoodsController {

    @DubboReference
    private SeckillGoodsService seckillGoodsService;

    /**
     * 添加秒杀商品
     * @param seckillGoods 秒杀商品实体
     * @return 操作结果
     */
    @PostMapping("/add")
    public BaseResult add(@RequestBody SeckillGoods seckillGoods) {
        seckillGoodsService.add(seckillGoods);
        return BaseResult.ok();
    }


    /**
     * 修改秒杀商品
     * @param seckillGoods 秒杀商品实体
     * @return 操作结果
     */
    @PutMapping("/update")
    public BaseResult update(@RequestBody SeckillGoods seckillGoods) {
        seckillGoodsService.update(seckillGoods);
        return BaseResult.ok();
    }


    /**
     * 分页查询秒杀商品
     * @param page 页数
     * @param size 每页条数
     * @return 查询结果
     */
    @GetMapping("/findPage")
    public BaseResult<Page<SeckillGoods>> findPage(int page, int size) {
        Page<SeckillGoods> seckillGoodsPage = seckillGoodsService.findPage(page, size);
        return BaseResult.ok(seckillGoodsPage);
    }
}

