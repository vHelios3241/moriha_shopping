package com.moriha.shopping_cart_customer_api.controller;

import com.moriha.common.pojo.CartGoods;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.CartService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/user/cart")
@RestController
public class CartController {

    @DubboReference
    private CartService cartService;

    /*
     * 查询用户购物车
     * @param userId 令牌中携带的用户Id
     * @return 用户购物车列表
     */
    @GetMapping("/findCartList")
    public BaseResult<List<CartGoods>> findCartList(@RequestHeader Long userId){
        List<CartGoods> cartList = cartService.findCartList(userId);
        return BaseResult.ok(cartList);
    }

    /*
     * 新增商品到购物车
     * @param cartGoods 购物车商品
     * @param userId 令牌中携带的用户Id
     * @return 操作结果
     */
    @PostMapping("/addCart")
    public BaseResult addCart(@RequestHeader Long userId, @RequestBody CartGoods cartGoods){
        cartService.addCart(userId, cartGoods);
        return BaseResult.ok();
    }

    /*
     * 修改购物车商品数量
     * @param userId 令牌中携带的用户Id
     * @param goodId 商品id
     * @param num 修改后的数量
     * @return 操作结果
     */
    @PutMapping("/handleCart")
    public BaseResult handleCart(@RequestHeader Long userId, Long goodId, Integer num){
        cartService.handleCart(userId, goodId, num);
        return BaseResult.ok();
    }

    /*
     * 删除购物车商品
     * @param userId 令牌中携带的用户Id
     * @param goodId 商品id
     * @return 操作结果
     */
    @DeleteMapping("/deleteCart")
    public BaseResult deleteCart(@RequestHeader Long userId, Long goodId){
        cartService.deleteCartOption(userId, goodId);
        return BaseResult.ok();
    }
}
