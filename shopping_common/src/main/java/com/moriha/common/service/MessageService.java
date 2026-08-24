package com.moriha.common.service;

import com.moriha.common.result.BaseResult;

/*
 * 短信服务
 */
public interface MessageService {
    // 发送短信
    BaseResult sendMessage(String phoneNumber, String code);
}
