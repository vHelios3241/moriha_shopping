package com.moriha.common.service;

import com.moriha.common.pojo.CartGoods;

import java.util.List;

/*
 *  购物车服务
 */
public interface CartService {
    // 新增商品到购物车
    void addCart(Long userId, CartGoods cartGoods);
    // 修改购物车商品数量
    void handleCart(Long userId, Long goodId, Integer num);
    // 删除购物车商品
    void deleteCartOption(Long userId, Long goodId);
    // 获取用户购物车
    List<CartGoods> findCartList(Long userId);
    // 更新redis中的商品数据，在管理员更新商品后执行
    void refreshCartGoods(CartGoods cartGoods);
    // 删除redis中的商品数据，在管理员下架商品后执行
    void deleteCartGoods(Long goodId);
}
