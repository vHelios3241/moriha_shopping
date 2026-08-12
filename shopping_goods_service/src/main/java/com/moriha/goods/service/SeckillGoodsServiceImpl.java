package com.moriha.goods.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.SeckillGoods;
import com.moriha.goods.mapper.SeckillGoodsMapper;
import org.apache.dubbo.config.annotation.DubboService;
import com.moriha.common.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    /**
     * 添加秒杀商品
     * @param seckillGoods 秒杀商品实体
     */
    @Override
    public void add(SeckillGoods seckillGoods) {
        seckillGoodsMapper.insert(seckillGoods);
    }

    /**
     * 修改秒杀商品
     * @param seckillGoods 秒杀商品实体
     */
    @Override
    public void update(SeckillGoods seckillGoods) {
        seckillGoodsMapper.updateById(seckillGoods);
    }

    /**
     * 分页查询秒杀商品
     * @param page 页数
     * @param size 每页条数
     * @return 查询结果
     */
    @Override
    public Page<SeckillGoods> findPage(int page, int size) {
        return seckillGoodsMapper.selectPage(new Page(page, size), null);
    }
}
