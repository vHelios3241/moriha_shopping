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

    /*
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

    /*
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

    /*
     * 用户注册
     * @param shoppingUser 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public BaseResult register(@RequestBody ShoppingUser shoppingUser){
        shoppingUserService.register(shoppingUser);
        return BaseResult.ok();
    }

    /*
     * 用户名密码登录
     * @param shoppingUser 用户对象
     * @return 登录结果
     */
    @PostMapping("/loginPassword")
    public BaseResult loginPassword(@RequestBody ShoppingUser shoppingUser){
        String sign = shoppingUserService.loginPassword(shoppingUser.getUsername(), shoppingUser.getPassword());
        // 返回JWT令牌
        return BaseResult.ok(sign);
    }

    /*
     * 发送登录短信验证码
     *
     * @param phone 手机号
     * @return 操作结果
     */
    @GetMapping("/sendLoginCheckCode")
    public BaseResult sendLoginCheckCode(String phone){
        // 0.判断用户手机号是否存在，状态是否正常
        shoppingUserService.checkPhone(phone);
        // 1.生成随机四位数
        String code = RandomUtil.buildCheckCode(4);
        // 2.发送短信
        BaseResult baseResult = messageService.sendMessage(phone, code);
        // 3.发送成功，将验证码保存到redis中；发送失败，返回发送结果
        if (200 == baseResult.getCode()){
            shoppingUserService.saveLoginCheckCode(phone, code);
            return BaseResult.ok();
        }else{
            return baseResult;
        }
    }

    /*
     * 手机号验证码登录
     * @param phone 手机号
     * @param checkCode 验证码
     * @return 登录结果
     */
    @PostMapping("/loginCheckCode")
    public BaseResult loginCheckCode(String phone, String checkCode){
        String sign = shoppingUserService.loginCheckCode(phone, checkCode);
        // 返回JWT令牌
        return BaseResult.ok(sign);
    }

    /*
     * 获取登录的用户名
     * @param authorization 令牌
     * @return 用户名
     * 从HTTP请求头拿名字叫`authorization`的值。
     */
    @GetMapping("/getName")
    public BaseResult<String> getName(@RequestHeader("authorization") String authorization){
        // 删掉请求头里的Bearer前缀，剥离出原始token
        String bearer = authorization.replace("Bearer", "");
        // 拿着纯净的token调用业务层，查询用户名
        String name = shoppingUserService.getName(bearer);
        return BaseResult.ok(name);
    }
}
