package com.moriha.common.service;

/*
 * AI客服服务
 */
public interface AICustCareService {
    // 根据用户消息生成回复
    String generateResponse(String userMessage);
}
