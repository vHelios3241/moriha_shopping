package com.moriha.shopping_custcare_service.Config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import com.moriha.common.pojo.SensitiveWord;
import com.moriha.shopping_custcare_service.mapper.SensitiveWordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

/*
 * 敏感词配置类
 */
@Configuration
public class SensitiveWordConfig {

    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;    // 从数据库中获取敏感词列表

    @Bean
    public SensitiveWordBs sensitiveWordBs() {
        return SensitiveWordBs.newInstance()
                // 黑名单：默认+自定义
                .wordDeny(WordDenys.chains(WordDenys.defaults(), new IWordDeny() {
                    @Override
                    public List<String> deny() {
                        QueryWrapper<SensitiveWord> wrapper = new QueryWrapper<>();
                        wrapper.eq("type", "deny");
                        return sensitiveWordMapper.selectList(wrapper)
                                .stream()
                                .map(SensitiveWord::getWord)
                                .collect(Collectors.toList());
                    }
                }))

                // 白名单：默认+自定义
                .wordAllow(WordAllows.chains(WordAllows.defaults(), new IWordAllow() {
                    @Override
                    public List<String> allow() {
                        QueryWrapper<SensitiveWord> wrapper = new QueryWrapper<>();
                        wrapper.eq("type", "allow");
                        return sensitiveWordMapper.selectList(wrapper)
                                .stream()
                                .map(SensitiveWord::getWord)
                                .collect(Collectors.toList());
                    }
                }))

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