package com.moriha.shopping_cart_service.service;

import com.moriha.common.pojo.CartGoods;
import com.moriha.common.service.CartService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

@DubboService
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void addCart(Long userId, CartGoods cartGoods) {

    }

    @Override
    public void handleCart(Long userId, Long goodId, Integer num) {

    }

    @Override
    public void deleteCartOption(Long userId, Long goodId) {

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

    @Override
    public void refreshCartGoods(CartGoods cartGoods) {

    }

    @Override
    public void deleteCartGoods(Long goodId) {

    }
}
