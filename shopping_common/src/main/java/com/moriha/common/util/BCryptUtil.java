package com.moriha.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/*
 * BCrypt 加密工具类
 */
public class BCryptUtil {

    // 定义静态成员变量
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /*
     * 加密
     * @param text 明文
     * @return 密文
     */
    public static String encode(String text){
        return encoder.encode(text);
    }

    /*
     * 验证
     * @param text 用户输入的明文密码
     * @param cipher 数据库存好的加密后的密文
     * @return 验证结果
     */
    public static boolean verify(String text, String cipher){
        return encoder.matches(text, cipher);
    }

}
