package com.moriha.common.service;

import com.moriha.common.result.BaseResult;

/*
 * 邮件服务
 */
public interface MailService {
    /**
     * 发送邮件
     * @param to 收件人邮箱
     * @param text 邮件正文
     * @param title 标题
     * @return 返回结果
     */
    BaseResult sendMail(String to, String text, String title);
}
