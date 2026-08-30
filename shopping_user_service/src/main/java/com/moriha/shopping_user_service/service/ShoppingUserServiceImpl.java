package com.moriha.shopping_user_service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.moriha.common.pojo.ShoppingUser;
import com.moriha.common.result.BusException;
import com.moriha.common.result.CodeEnum;
import com.moriha.common.service.ShoppingUserService;
import com.moriha.common.util.Md5Util;
import com.moriha.shopping_user_service.mapper.ShoppingUserMapper;
import com.moriha.shopping_user_service.util.JwtUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Map;
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
        // 1.验证用户名
        QueryWrapper<ShoppingUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        ShoppingUser shoppingUser = shoppingUserMapper.selectOne(queryWrapper);
        if(shoppingUser == null){
            throw new BusException(CodeEnum.LOGIN_NAME_PASSWORD_ERROR);
        }
        // 2.验证密码
        boolean verify = Md5Util.verify(password, shoppingUser.getPassword());
        if (!verify) {
            throw new BusException(CodeEnum.LOGIN_NAME_PASSWORD_ERROR);
        }
        // 3.生成JWT令牌，返回令牌
        String token = JwtUtils.sign(shoppingUser.getId(), shoppingUser.getUsername());

        return token;
    }

    /*
     * 登录时向redis保存手机号+验证码
     * @param phone
     * @param checkCode
     */
    @Override
    public void saveLoginCheckCode(String phone, String checkCode) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // redis键为手机号，值为验证码，过期时间5分钟
        valueOperations.set("loginCode:" + phone, checkCode, 300, TimeUnit.SECONDS);
    }

    /*
     * 手机号+验证码 登录
     * @param phone
     * @param checkCode
     * @return
     */
    @Override
    public String loginCheckCode(String phone, String checkCode) {
        // 验证用户传入的手机号验证码是否在redis中存在
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object checkCodeRedis = valueOperations.get("loginCode:" + phone);
        if (!checkCode.equals(checkCodeRedis)) {
            throw new BusException(CodeEnum.LOGIN_CODE_ERROR);
        }
        // 登录成功，查询用户
        QueryWrapper<ShoppingUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        ShoppingUser shoppingUser = shoppingUserMapper.selectOne(queryWrapper);
        // 生成JWT令牌，返回令牌
        String token = JwtUtils.sign(shoppingUser.getId(), shoppingUser.getUsername());

        return token;
    }

    /*
     * 根据令牌获取用户名
     */
    @Override
    public String getName(String token) {
        Map<String, Object> verify = JwtUtils.verify(token);
        String username = (String) verify.get("username");
        return username;
    }

    /*
     * 根据令牌获取用户信息
     */
    @Override
    public ShoppingUser getLoginUser(String token) {
        // 从令牌中获取用户id
        Map<String, Object> verify = JwtUtils.verify(token);
        Long userId = (Long) verify.get("userId");
        // 根据id查询用户
        QueryWrapper<ShoppingUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        ShoppingUser shoppingUser = shoppingUserMapper.selectOne(queryWrapper);
        return shoppingUser;

    }

    /*
     * 判断用户手机号是否存在，状态是否正常
     * @param phone
     */
    @Override
    public void checkPhone(String phone) {
        // 手机号是否存在
        QueryWrapper<ShoppingUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        ShoppingUser shoppingUser = shoppingUserMapper.selectOne(queryWrapper);
        if(shoppingUser == null){
            throw new BusException(CodeEnum.LOGIN_NOPHONE_ERROR);
        }
        // 用户状态是否正常
        if(!"Y".equals(shoppingUser.getStatus())){
            throw new BusException(CodeEnum.LOGIN_USER_STATUS_ERROR);
        }
    }
}
