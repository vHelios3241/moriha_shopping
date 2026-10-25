package com.moriha.shopping_custcare_service.service;

import com.moriha.common.service.AICustCareService;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Map;

@DubboService
public class AICustCareServiceImpl implements AICustCareService {

    private static final Logger log = LoggerFactory.getLogger(AICustCareServiceImpl.class);
    private static final String FALLBACK_MESSAGE = "非常抱歉，当前客服系统繁忙，请稍后再试。";
    private static final String RATE_LIMIT_MESSAGE = "非常抱歉，当前咨询人数较多，请稍后再试哦~";

    @Autowired
    private ChatClient chatClient;
    @Value("${spring.ai.dashscope.prompt}")
    private String prompt;

    /**
     * 根据用户消息生成响应
     *
     * @param userMessage 用户消息
     * @return AI的响应
     */
    @Override
    public String generateResponse(String userMessage) {
        try {
            // 定义带占位符的提示词模板，将真实日期注入模板
            // 加载带占位符的模板文本
            PromptTemplate promptTemplate = new PromptTemplate(prompt);
            // 替换占位符为真实日期
            String resolvedPrompt = promptTemplate.render(
                    Map.of("current_date", LocalDate.now().toString())
            );
            // 链式调用，大模型对话客户端: 组装发给大模型的消息.设置系统提示词.设置用户消息.真正发起 HTTP 请求调用大模型接口.提取AI回答的文本字符串
            String content = chatClient.prompt().system(resolvedPrompt).user(userMessage).call().content();
            return content;
        } catch (Exception e) {
            log.error("AI客服生成响应异常, userMessage={}", userMessage, e);
            return buildFallbackMessage(e);
        }
    }

    /**
     * 根据异常类型构建兜底提示语
     */
    private String buildFallbackMessage(Exception e) {
        String message = e.getMessage();
        if (message != null && (message.contains("429") || message.contains("RateQuota"))) {
            return RATE_LIMIT_MESSAGE;
        }
        return FALLBACK_MESSAGE;
    }
}