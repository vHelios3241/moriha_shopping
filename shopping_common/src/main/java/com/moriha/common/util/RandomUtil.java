package com.moriha.common.util;

import java.util.Random;

public class RandomUtil {
    /**
     * 生成验证码
     * @param digit 位数
     * @return
     */
    public static String buildCheckCode(int digit){
        String str = "0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i<digit; i++){
            char ch = str.charAt(random.nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }
}
