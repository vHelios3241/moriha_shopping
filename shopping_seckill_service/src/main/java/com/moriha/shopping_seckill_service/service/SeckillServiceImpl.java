package com.moriha.shopping_seckill_service.service;

import cn.hutool.bloomfilter.BitMapBloomFilter;
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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@DubboService
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private BitMapBloomFilter filter;

    /**
     * 每分钟查询一次数据库，更新redis中的秒杀商品数据
     * 条件为startTime <= 当前时间 <= endTime，库存大于0
     */
    @Scheduled(cron = "0 * * * * *")
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
            redisTemplate.boundHashOps("seckillGoods").put(String.valueOf(seckillGoods.getGoodsId()), seckillGoods);
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
        // 1.从redis中查询秒杀商品
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("seckillGoods").get(String.valueOf(goodsId));
        // 2.如果查到商品，返回
        if(seckillGoods != null){
            return seckillGoods;
        }
        // 3.如果没有查到商品，从数据库查询秒杀商品
        QueryWrapper<SeckillGoods> queryWrapper = new QueryWrapper();
        queryWrapper.eq("goodsId",goodsId);
        SeckillGoods seckillGoodsMysql = seckillGoodsMapper.selectOne(queryWrapper);
        System.out.println("从mysql中查询秒杀商品");
        // 4.如果该商品不在秒杀状态，抛出异常
        Date now = new Date();
        if(seckillGoodsMysql == null
        || now.before(seckillGoodsMysql.getStartTime())
        || now.after(seckillGoodsMysql.getEndTime())
        || seckillGoodsMysql.getStockCount() <= 0){
            return null;
        }
        // 5.如果该商品在秒杀状态，将商品保存到redis，并返回该商品
        addRedisSeckillGoods(seckillGoodsMysql);
        return seckillGoodsMysql;
    }

    /*
     * 生成秒杀订单
     * @param orders 订单数据
     * @return
     */
    @Override
    public Orders createOrder(Orders orders) {

        // 将redis中秒杀商品的库存数据同步到mysql
        List<SeckillGoods> seckillGoodsList = redisTemplate.boundHashOps("seckillGoods").values();
        for (SeckillGoods seckillGoods : seckillGoodsList) {
            // 在数据库查询秒杀商品
            QueryWrapper<SeckillGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("goodsId", seckillGoods.getGoodsId());
            SeckillGoods sqlSeckillGoods = seckillGoodsMapper.selectOne(queryWrapper);
            // 修改数据库中秒杀商品的库存，和redis中的库存保持一致
            sqlSeckillGoods.setStockCount(seckillGoods.getStockCount());
            seckillGoodsMapper.updateById(sqlSeckillGoods);
        }

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
        redisTemplate.boundHashOps("seckillGoods").put(String.valueOf(seckillGoods.getGoodsId()),seckillGoods);

        // 3.保存订单数据 (手动把 key 序列化器改成 String)
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置订单过期时间
        redisTemplate.opsForValue().set(orders.getId(), orders, 1, TimeUnit.MINUTES);
        /**
         * 给订单创建副本，副本的过期时间长于原订单
         * redis过期后触发过期事件时，redis数据已经过期，此时只能拿到key，拿不到value。
         * 而过期事件需要回退商品库存，必须拿到value即订单详情，才能拿到商品数据，进行回退操作
         * 我们保存一个订单副本，过期时间长于原订单，此时就可以通过副本拿到原订单数据
         */
        redisTemplate.opsForValue().set(orders.getId()+"_copy", orders, 2, TimeUnit.MINUTES);

        return orders;
    }

    /*
     * 根据id查询秒杀订单
     * @param id 订单id
     * @return
     */
    @Override
    public Orders findOrder(String id) {
        return (Orders) redisTemplate.opsForValue().get(id);
    }

    /*
     * 支付秒杀订单
     * @param orderId 订单id
     * @return
     */
    @Override
    public Orders pay(String orderId) {
        // 1.查询订单，设置数据
        Orders orders = findOrder(orderId);
        if (orders == null){
            throw new BusException(CodeEnum.ORDER_EXPIRED_ERROR);
        }
        orders.setStatus(2); // 已付款
        orders.setPaymentTime(new Date());
        orders.setPaymentType(2); // 支付宝支付
        // 2.从redis删除订单数据
        redisTemplate.delete(orderId);
        redisTemplate.delete(orderId + "_copy");
        // 3.返回订单数据
        return orders;
    }

    /*
     * 将一个秒杀商品保存到redis中
     * @param seckillGoods 秒杀商品对象
     */
    @Override
    public void addRedisSeckillGoods(SeckillGoods seckillGoods) {
        redisTemplate.boundHashOps("seckillGoods").put(String.valueOf(seckillGoods.getGoodsId()),seckillGoods);
    }
}
