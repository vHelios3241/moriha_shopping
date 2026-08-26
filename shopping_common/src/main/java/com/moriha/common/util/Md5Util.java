package com.moriha.common.util;

import org.apache.commons.codec.digest.DigestUtils;

/*
 * MD5加密工具类
 */
public class Md5Util {
    public final static String md5key = "BAIZHAN"; // 盐值

    /**
     * 加密
     * @param text 明文（比如用户输入的密码 123456）
     * @return 密文（一串32位十六进制字符串）
     */
    public static String encode(String text){
        return DigestUtils.md5Hex(text + md5key);
    }

    /**
     * 验证
     * @param text 用户输入的明文密码
     * @param cipher 数据库存好的加密后的密文
     * @return true密码正确，false密码错误
     */
    public static boolean verify(String text, String cipher) {
        String encode = encode(text);
        if (!encode.equals(cipher)) {
            return false;
        }
        return true;
    }
}
