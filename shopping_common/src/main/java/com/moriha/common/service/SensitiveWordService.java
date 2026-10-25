package com.moriha.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.SensitiveWord;
import org.springframework.stereotype.Service;

/*
 * 敏感词黑/白名单服务接口
 * 提供敏感词黑/白名单相关的业务功能
 */
@Service
public interface SensitiveWordService{
    /**
     * 添加敏感词
     * @param sensitiveWord 敏感词对象
     */
    void addSensitiveWord(SensitiveWord sensitiveWord);

    /**
     * 删除敏感词
     * @param id 敏感词ID
     */
    void deleteSensitiveWord(Long id);

    /**
     * 分页查询敏感词
     * @param page 页码
     * @param size 每页大小
     * @param word 关键词（可选，模糊匹配）
     * @param type 敏感词类型 ("deny" 或 "allow")
     * @return 分页结果
     */
    Page<SensitiveWord> findSensitiveWords(int page, int size, String word, String type);
}
