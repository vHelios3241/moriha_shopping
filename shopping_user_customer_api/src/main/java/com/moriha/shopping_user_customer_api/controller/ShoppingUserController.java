package com.moriha.shopping_user_customer_api.controller;

import com.moriha.common.pojo.ShoppingUser;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.MessageService;
import com.moriha.common.service.ShoppingUserService;
import com.moriha.common.util.RandomUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/shoppingUser")
public class ShoppingUserController {

    @DubboReference
    private ShoppingUserService shoppingUserService;
    @DubboReference
    private MessageService messageService;

    /**
     * 发送注册短信
     * @param phone 注册手机号
     * @return 操作结果
     */
    @GetMapping("/sendMessage")
    public BaseResult sendMessage(String phone){
        // 1.生成随机四位数
        String code = RandomUtil.buildCheckCode(4);
        // 2.发送短信
        BaseResult baseResult = messageService.sendMessage(phone, code);
        // 3.发送成功，将验证码保存到redis中,发送失败，返回发送结果
        if (200 == baseResult.getCode()){
            shoppingUserService.saveRegisterCheckCode(phone, code);
            return BaseResult.ok();
        }else{
            return baseResult;
        }
    }

    /**
     * 验证用户注册验证码
     * @param phone 手机号
     * @param checkCode 验证码
     * @return 200验证成功，605验证码不正确
     */
    @GetMapping("/registerCheckCode")
    public BaseResult register(String phone, String checkCode){
        shoppingUserService.registerCheckCode(phone, checkCode);
        return BaseResult.ok();
    }

    /**
     * 用户注册
     * @param shoppingUser 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public BaseResult register(@RequestBody ShoppingUser shoppingUser){
        shoppingUserService.register(shoppingUser);
        return BaseResult.ok();
    }
}
