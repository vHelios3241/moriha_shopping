package com.moriha.shopping_custcare_service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.moriha.common.pojo.SensitiveWord;
import org.apache.ibatis.annotations.Mapper;

/*
 * 敏感词黑/白名单 Mapper接口
 */
@Mapper
public interface SensitiveWordMapper extends BaseMapper<SensitiveWord> {
}