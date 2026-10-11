package com.moriha.shopping_custcare_service.service;

import com.moriha.common.service.AICustCareService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;
import java.util.Map;

public class AICustCareServiceImpl implements AICustCareService {

    @Autowired
    private ChatClient chatClient;
    @Value("${spring.ai.dashscope.prompt}")
    private String prompt;

    @Override
    public String generateResponse(String userMessage) {
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
        }
}