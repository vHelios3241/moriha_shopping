package com.moriha.shopping_seckill_service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.CartGoods;
import com.moriha.common.pojo.Orders;
import com.moriha.common.pojo.SeckillGoods;
import com.moriha.common.result.BusException;
import com.moriha.common.result.CodeEnum;
import com.moriha.common.service.SeckillService;
import com.moriha.shopping_seckill_service.mapper.SeckillGoodsMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
@DubboService
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 每分钟查询一次数据库，更新redis中的秒杀商品数据
     * 条件为startTime <= 当前时间 <= endTime，库存大于0
     */
    @Scheduled(cron = "0/5 * * * * *")
    public void refreshRedis() {
        System.out.println("同步mysql秒杀商品到redis...");

        // 1.查询数据库中正在秒杀的商品
        QueryWrapper<SeckillGoods> queryWrapper = new QueryWrapper<>();
        Date date = new Date();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        queryWrapper.le("startTime", now) // 当前时间晚于开始时间
                .ge("endTime", now)  // 当前时间早于结束时间
                .gt("stockCount", 0); // 库存大于0
        List<SeckillGoods> seckillGoodsList = seckillGoodsMapper.selectList(queryWrapper);

        // 2.删除之前的秒杀商品
        redisTemplate.delete("seckillGoods");

        // 3.保存现在正在秒杀的商品
        for (SeckillGoods seckillGoods : seckillGoodsList) {
            redisTemplate.boundHashOps("seckillGoods").put(seckillGoods.getGoodsId(), seckillGoods);
        }
    }

    /*
     * 前台用户分页查询秒杀商品
     * @param page 页数
     * @param size 每页条数
     * @return 查询结果
     */
    @Override
    public Page<SeckillGoods> findPageByRedis(int page, int size) {
        // 1.查询所有秒杀商品列表
        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps("seckillGoods").values();
        // 2.获取当前页商品列表
        int start = (page - 1) * size;
        int end = start + size > seckillGoodsList.size() ? seckillGoodsList.size() : start + size;
        // 获取当前页结果集
        List<SeckillGoods> seckillGoods = seckillGoodsList.subList(start, end);
        // 3.构造页面对象
        Page<SeckillGoods> page1 = new Page<>();
        page1.setCurrent(page)  // 当前页
                .setSize(size)  // 每页条数
                .setTotal(seckillGoodsList.size())  // 总条数
                .setRecords(seckillGoods);  // 结果集

        return page1;
    }

    /*
     * 查询秒杀商品详情
     * @param goodsId 秒杀商品对应的商品Id
     * @return 查询结果
     */
    @Override
    public SeckillGoods findSeckillGoodsByRedis(Long goodsId) {
        return (SeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(goodsId);
    }

    /*
     * 生成秒杀订单
     * @param orders 订单数据
     * @return
     */
    @Override
    public Orders createOrder(Orders orders) {

        // 1.生成订单对象
        orders.setId(IdWorker.getIdStr()); // 手动生产订单id
        orders.setStatus(1); // 订单状态未付款
        orders.setCreateTime(new Date()); // 订单创建时间
        orders.setExpire(new Date(new Date().getTime() + 1000*60*5)); // 订单过期时间
        // 计算商品价格
        CartGoods cartGoods = orders.getCartGoods().get(0);
        Integer num = cartGoods.getNum();
        BigDecimal price = cartGoods.getPrice();
        BigDecimal sum = price.multiply(BigDecimal.valueOf(num));
        orders.setPayment(sum);

        // 2.减少秒杀商品库存
        // 查询秒杀商品
        SeckillGoods seckillGoods = findSeckillGoodsByRedis(cartGoods.getGoodId());
        // 查询库存，库存不足抛出异常
        Integer stockCount = seckillGoods.getStockCount();
        if (stockCount <= 0){
            throw new BusException(CodeEnum.NO_STOCK_ERROR);
        }
        // 减少库存
        seckillGoods.setStockCount(seckillGoods.getStockCount() - cartGoods.getNum());
        // 更新redis中的秒杀商品数据
        redisTemplate.boundHashOps("seckillGoods").put(seckillGoods.getGoodsId(),seckillGoods);

        // 3.保存订单数据
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(orders.getId(),orders);
        return orders;
    }

    /*
     * 根据id查询秒杀订单
     * @param id 订单id
     * @return
     */
    @Override
    public Orders findOrder(String id) {
        return null;
    }

    /*
     * 支付秒杀订单
     * @param orderId 订单id
     * @return
     */
    @Override
    public Orders pay(String orderId) {
        return null;
    }

}
