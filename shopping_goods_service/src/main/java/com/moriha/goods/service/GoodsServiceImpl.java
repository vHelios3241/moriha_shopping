package com.moriha.goods.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Goods;
import com.moriha.common.pojo.GoodsImage;
import com.moriha.common.pojo.Specification;
import com.moriha.common.pojo.SpecificationOption;
import com.moriha.common.service.GoodsService;
import com.moriha.goods.mapper.GoodsImageMapper;
import com.moriha.goods.mapper.GoodsMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@DubboService
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsImageMapper goodsImageMapper;

    /**
     * 新增商品
     *
     * @param goods
     */
    @Override
    public void add(Goods goods) {
        //1.插入商品数据
        goodsMapper.insert(goods);
        //2.插入商品图片数据
        Long goodsId = goods.getId();
        List<GoodsImage> images = goods.getImages();
        for (GoodsImage image : images) {
            image.setGoodsId(goodsId);
            goodsImageMapper.insert(image);
        }
        //3.插入商品规格项数据
        // 获取规格
        List<Specification> specifications = goods.getSpecifications();
        // 获取规格项
        List<SpecificationOption> options = new ArrayList(); //规格项集合
        // 遍历规格，获取规格中的所有规格项
        for (Specification specification : specifications) {
            options.addAll(specification.getSpecificationOptions());
        }
        // 遍历规格项，插入商品_规格项数据
        for (SpecificationOption option : options) {
            goodsMapper.addGoodsSpecificationOption(goodsId, option.getId());
        }
    }

    /**
     * 修改商品
     *
     * @param goods
     */
    @Override
    public void update(Goods goods) {
        Long goodsId = goods.getId(); // 商品id
        // 删除旧图片数据
        QueryWrapper<GoodsImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goodsId", goods.getId());
        goodsImageMapper.delete(queryWrapper);
        // 删除旧规格项数据
        goodsMapper.deleteGoodsSpecificationOption(goodsId);
        // 插入商品数据
        goodsMapper.updateById(goods);
        // 插入图片数据
        List<GoodsImage> images = goods.getImages(); // 商品图片
        for (GoodsImage image : images) {
            image.setGoodsId(goodsId); // 给图片设置商品id
            goodsImageMapper.insert(image); // 插入图片
        }
        // 插入商品_规格项数据
        List<Specification> specifications = goods.getSpecifications(); // 获取规格
        List<SpecificationOption> options = new ArrayList(); // 规格项集合
        // 遍历规格，获取规格中的所有规格项
        for (Specification specification : specifications) {
            options.addAll(specification.getSpecificationOptions());
        }
        // 遍历规格项，插入商品_规格项数据
        for (SpecificationOption option : options) {
            goodsMapper.addGoodsSpecificationOption(goodsId, option.getId());
        }
    }

    /**
     * 上下架商品
     *
     * @param id
     * @param isMarketable
     */
    @Override
    public void putAway(Long id, Boolean isMarketable) {
        goodsMapper.putAway(id, isMarketable);
    }

    /**
     * 根据id查询商品
     *
     * @param id
     * @return
     */
    @Override
    public Goods findById(Long id) {
        return goodsMapper.findById(id);
    }

    /**
     * 根据条件查询商品列表
     *
     * @param goods
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Goods> search(Goods goods, int page, int size) {
        QueryWrapper<Goods> queryWrapper = new QueryWrapper();
        // 判断商品名不为空
        if (goods != null && StringUtils.hasText(goods.getGoodsName())) {
            queryWrapper.like("goodsName", goods.getGoodsName());
        }
        Page<Goods> page1 = goodsMapper.selectPage(new Page(page, size), queryWrapper);
        return page1;
    }
}
