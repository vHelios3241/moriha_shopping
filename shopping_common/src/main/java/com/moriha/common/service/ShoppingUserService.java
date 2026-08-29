package com.moriha.common.service;

import com.moriha.common.pojo.ShoppingUser;

/*
 * 商城用户服务
 */
public interface ShoppingUserService {
    // 注册时向redis保存手机号+验证码
    void saveRegisterCheckCode(String phone, String code);
    // 注册时验证手机号+验证码
    void registerCheckCode(String phone, String code);
    // 用户注册
    void register(ShoppingUser user);

    // 用户名+密码 登录
    String loginPassword(String username, String password);
    // 手机号+验证码 登录
    String loginCheckCode(String phone, String checkCode);
    // 登录时向redis保存手机号+验证码
    void saveLoginCheckCode(String phone,String checkCode);

    // 获取登录用户名
    String getName(String token);
    // 根据id获取用户
    ShoppingUser getLoginUser(Long id);

    //判断用户手机号是否存在，状态是否正常
    void checkPhone(String phone);
}
