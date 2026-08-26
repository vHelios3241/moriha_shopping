package com.moriha.shopping_user_service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moriha.common.pojo.ShoppingUser;
import com.moriha.common.result.BusException;
import com.moriha.common.result.CodeEnum;
import com.moriha.common.service.ShoppingUserService;
import com.moriha.common.util.Md5Util;
import com.moriha.shopping_user_service.mapper.ShoppingUserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@DubboService
public class ShoppingUserServiceImpl implements ShoppingUserService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ShoppingUserMapper shoppingUserMapper;


    /*
     * 注册时 向redis保存手机号+验证码
     * @param phone
     * @param code
     */
    @Override
    public void saveRegisterCheckCode(String phone, String code) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // redis键为手机号，值为验证码，过期时间5分钟
        valueOperations.set("registerCode:" + phone, code, 300, TimeUnit.SECONDS);
    }

    /*
     * 注册时 验证手机号+验证码
     * @param phone
     * @param code
     */
    @Override
    public void registerCheckCode(String phone, String code) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String redisCode = (String) valueOperations.get("registerCode:" + phone);
        if(!code.equals(redisCode)){
            throw new BusException(CodeEnum.REGISTER_CODE_ERROR);
        }
    }

    /*
     * 注册
     * @param shoppingUser
     */
    @Override
    public void register(ShoppingUser shoppingUser) {
        // 验证手机号是否存在
        String phone = shoppingUser.getPhone();
        QueryWrapper<ShoppingUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        Long count = shoppingUserMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new BusException(CodeEnum.REGISTER_REPEAT_PHONE_ERROR);
        }
        // 验证用户名是否存在
        String username = shoppingUser.getUsername();
        QueryWrapper<ShoppingUser> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("username", username);
        Long count1 = shoppingUserMapper.selectCount(queryWrapper1);
        if(count1 > 0){
            throw new BusException(CodeEnum.REGISTER_REPEAT_NAME_ERROR);
        }
        // 新增用户
        shoppingUser.setStatus("Y");
        shoppingUser.setPassword(Md5Util.encode(shoppingUser.getPassword()));
        shoppingUserMapper.insert(shoppingUser);
    }

    /*
     * 用户名+密码 登录
     */
    @Override
    public String loginPassword(String username, String password) {
        return "";
    }

    @Override
    public String loginCheckCode(String phone, String checkCode) {
        return "";
    }

    @Override
    public void saveLoginCheckCode(String phone, String checkCode) {

    }

    @Override
    public String getName(String token) {
        return "";
    }

    @Override
    public ShoppingUser getLoginUser(Long id) {
        return null;
    }
}
