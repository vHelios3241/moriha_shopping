package com.moriha.shopping_custcare_service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.moriha.common.pojo.SensitiveWord;
import com.moriha.common.service.SensitiveWordService;
import com.moriha.shopping_custcare_service.mapper.SensitiveWordMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/*
 * 敏感词服务实现类
 */
@DubboService
public class SensitiveWordServiceImpl implements SensitiveWordService {

    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;
    @Autowired
    private SensitiveWordBs swbs;

    /**
     * 添加敏感词
     * @param sw 敏感词对象
     */
    @Override
    public void addSensitiveWord(SensitiveWord sw) {
        sensitiveWordMapper.insert(sw);
        // 刷新敏感词配置
        if (sw.getType().equals("deny")){
            swbs.addWord(sw.getWord());
        }else if (sw.getType().equals("allow")){
            swbs.addWordAllow(sw.getWord());
        }
    }

    /**
     * 删除敏感词
     * @param id 敏感词ID
     */
    @Override
    public void deleteSensitiveWord(Long id) {
        sensitiveWordMapper.deleteById(id);

        SensitiveWord sw = sensitiveWordMapper.selectById(id);
        // 刷新敏感词配置
        if (sw.getType().equals("deny")){
            swbs.removeWord(sw.getWord());
        }else if (sw.getType().equals("allow")){
            swbs.removeWordAllow(sw.getWord());
        }
    }

    /**
     * 分页查询敏感词
     * @param page 页码
     * @param size 每页大小
     * @param word 关键词（可选，模糊匹配）
     * @param type 敏感词类型 ("deny" 或 "allow")
     * @return
     */
    @Override
    public Page<SensitiveWord> findSensitiveWords(int page, int size, String word ,String type) {
        QueryWrapper<SensitiveWord> wrapper = new QueryWrapper<>();
        if (word != null && !word.isEmpty()){
            wrapper.like("word", word);
        }
        if (type != null && !type.isEmpty()){
            wrapper.eq("type", type);
        }
        return sensitiveWordMapper.selectPage(new Page<>(page, size), wrapper);
    }
}