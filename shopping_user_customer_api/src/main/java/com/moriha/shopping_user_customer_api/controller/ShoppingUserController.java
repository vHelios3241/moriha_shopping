package com.moriha.shopping_user_customer_api.controller;

import com.moriha.common.result.BaseResult;
import com.moriha.common.service.MessageService;
import com.moriha.common.service.ShoppingUserService;
import com.moriha.common.util.RandomUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
