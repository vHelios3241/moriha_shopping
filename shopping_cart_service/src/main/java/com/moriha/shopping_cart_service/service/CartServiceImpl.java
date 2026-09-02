package com.moriha.shopping_cart_service.service;

import com.moriha.common.pojo.CartGoods;
import com.moriha.common.service.CartService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@DubboService
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;


    /*
     * 添加商品到购物车
     */
    @Override
    public void addCart(Long userId, CartGoods cartGoods) {
        // 1.获取购物车列表
        List<CartGoods> cartList = findCartList(userId);
        // 2.查询购物车是否有该商品，如果有商品，添加商品数量
        for (CartGoods goods : cartList) {
            if (goods.getGoodId().equals(cartGoods.getGoodId())) {
                int newNum = goods.getNum() + cartGoods.getNum();
                goods.setNum(newNum);
                redisTemplate.boundHashOps("cartList").put(userId, cartList);
                return;
            }
            // 3.如果没有该商品，添加商品
            cartList.add(cartGoods);
            redisTemplate.boundHashOps("cartList").put(userId, cartList);

        }
    }

    /*
     * 修改购物车商品数量
     * @param userId
     * @param goodId
     * @param num
     */
    @Override
    public void handleCart(Long userId, Long goodId, Integer num) {
        // 1.获取购物车列表
        List<CartGoods> cartList = findCartList(userId);
        // 2.遍历列表找到对应商品
        for (CartGoods cartGoods : cartList) {
            if (goodId.equals(cartGoods.getGoodId())) {
                cartGoods.setNum(num);
                break;
            }
        }
        // 3.将新的购物车列表保存到redis中
        redisTemplate.boundHashOps("cartList").put(userId, cartList);
    }

    /*
     * 删除购物车商品
     * @param userId
     * @param goodId
     */
    @Override
    public void deleteCartOption(Long userId, Long goodId) {
        // 1.获取购物车列表
        List<CartGoods> cartList = findCartList(userId);
        // 2.遍历列表找到对应商品并删除
        for (CartGoods cartGoods : cartList) {
            if (goodId.equals(cartGoods.getGoodId())) {
                cartList.remove(cartGoods);
                break;
            }
        }
        // 3.将新的购物车列表保存到redis中
        redisTemplate.boundHashOps("cartList").put(userId, cartList);
    }

    /*
     * 查询购物车列表
     * @param userId
     *
     */
    @Override
    public List<CartGoods> findCartList(Long userId) {
        Object cartList = redisTemplate.boundHashOps("cartList").get(userId);
        if (cartList == null) {
            return new ArrayList<CartGoods>();
        } else {
            return (List<CartGoods>) cartList;
        }
    }

    /*
     * 修改所有用户购物车商品
     * @param cartGoods
     */
    @Override
    public void refreshCartGoods(CartGoods cartGoods) {
        // 获取所有用户的购物车
        BoundHashOperations cartList = redisTemplate.boundHashOps("cartList");
        Map<Long, List<CartGoods>> allCartGoods = cartList.entries();
        Collection<List<CartGoods>> values = allCartGoods.values();
        // 遍历所有用户购物车并更新商品信息
        for (List<CartGoods> value : values) {
            for (CartGoods goods : value) {
                if (goods.getGoodId().equals(cartGoods.getGoodId())) {
                    goods.setGoodsName(cartGoods.getGoodsName());
                    goods.setHeaderPic(cartGoods.getHeaderPic());
                    goods.setPrice(cartGoods.getPrice());
                }
            }
        }
        // 将改变后所有用户购物车重新放入redis
        redisTemplate.delete("cartList");
        redisTemplate.boundHashOps("cartList").putAll(allCartGoods);
    }


    /*
     * 删除所有用户购物车商品
     * @param goodId
     */
    @Override
    public void deleteCartGoods(Long goodId) {
        // 获取所有用户购物车
        BoundHashOperations cartList = redisTemplate.boundHashOps("cartList");
        Map<Long, List<CartGoods>> allCartGoods = cartList.entries();
        Collection<List<CartGoods>> values = allCartGoods.values();
        // 遍历所有用户购物车并删除已下架的商品
        for (List<CartGoods> goodsList : values) {
            for (CartGoods goods : goodsList) {
                if (goods.getGoodId().equals(goodId)){
                    goodsList.remove(goods);
                    break;
                }
            }
        }
        // 将改变后所有用户购物车重新放入redis
        redisTemplate.delete("cartList");
        redisTemplate.boundHashOps("cartList").putAll(allCartGoods);
    }




}

