package com.moriha.shopping_custcare_service.Config;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * 敏感词配置类
 */
@Configuration
public class SensitiveWordConfig {
    @Bean
    public SensitiveWordBs sensitiveWordBs() {
        return SensitiveWordBs.newInstance()
                // 黑名单：默认
                .wordDeny(WordDenys.chains(WordDenys.defaults()))
                // 白名单：默认
                .wordAllow(WordAllows.chains(WordAllows.defaults()))
                // 忽略大小写
                .ignoreCase(true)
                // 忽略全角/半角
                .ignoreWidth(true)
                // 启用邮箱检查
                .enableEmailCheck(true)
                // 启用URL检查
                .enableUrlCheck(true)
                // 启用数字检查
                .enableNumCheck(true)
                // 初始化
                .init();
    }
}
