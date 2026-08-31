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


    /*
     * 添加商品到购物车
     */
    @Override
    public void addCart(Long userId, CartGoods cartGoods) {
        // 1.获取购物车列表
        List<CartGoods> cartList = findCartList(userId);
        // 2.查询购物车是否有该商品，如果有商品，添加商品数量
        for (CartGoods goods : cartList) {
            if (goods.getGoodId().equals(cartGoods.getGoodId())){
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
            if (goodId.equals(cartGoods.getGoodId())){
                cartGoods.setNum(num);
                break;
            }
        }
        // 3.将新的购物车列表保存到redis中
        redisTemplate.boundHashOps("cartList").put(userId, cartList);
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
