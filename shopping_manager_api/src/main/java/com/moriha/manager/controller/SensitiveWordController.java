package com.moriha.manager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.SensitiveWord;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.SensitiveWordService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

/*
 * 敏感词管理控制器
 */
@RestController
@RequestMapping("/sensitiveWord")
public class SensitiveWordController {

    @DubboReference
    private SensitiveWordService sensitiveWordService;

    @PostMapping("/add")
    public BaseResult addSensitiveWord(@RequestBody SensitiveWord sensitiveWord) {
        sensitiveWordService.addSensitiveWord(sensitiveWord);
        return BaseResult.ok();
    }

    @DeleteMapping("/delete")
    public BaseResult deleteSensitiveWord(Long id) {
        sensitiveWordService.deleteSensitiveWord(id);
        return BaseResult.ok();
    }

    @GetMapping("/search")
    public BaseResult<Page<SensitiveWord>> search(@RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(required = false) String word,
                                                  @RequestParam(required = false) String type) {
        Page<SensitiveWord> sensitiveWords = sensitiveWordService.findSensitiveWords(page, size,word,type);
        return BaseResult.ok(sensitiveWords);
    }
}