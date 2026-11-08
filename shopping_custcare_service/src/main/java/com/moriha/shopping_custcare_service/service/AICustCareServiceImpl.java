package com.moriha.shopping_custcare_service.service;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.moriha.common.pojo.Faq;
import com.moriha.common.service.AICustCareService;
import com.moriha.common.service.FaqService;
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
    @Autowired
    private FaqService faqService;
    @Autowired
    private SensitiveWordBs sensitiveWordBs;
    @Value("${spring.ai.dashscope.prompt}")
    private String prompt;

    /**
     * 根据用户消息生成响应
     *
     * @param userMessage 用户消息
     * @return AI的响应
     */
    @Override
    public String generateResponse(String userMessage, Long userId) {
        try {
            // 添加当前时间+用户ID
            PromptTemplate promptTemplate = new PromptTemplate(prompt);
            // 替换占位符为真实时间+用户ID
            String resolvedPrompt = promptTemplate.render(
                    Map.of("current_date", LocalDate.now().toString(),
                            "user_id", userId )
            );

            // 1.检查敏感词
            String error = check(userMessage);
            if (error != null) {
                return error;
            }

            // 2.先尝试FAQ回答，如果没有答案再使用AI
            Faq faq = faqService.findBestAnswer(userMessage);
            if (faq != null) {
                return faq.getAnswer();
            }

            // 3.如果没有FAQ，调用AI模型回答
            prompt += """
                   你是一个专业的电商客服助手，专门处理订单查询和商品查询请求。请严格按照以下规则执行：
                   1. **意图识别**：
                   - 当用户提及"订单"、"我的订单"、"购买记录"、"待支付"、"已发货"、"最近订单"等关键词时，识别为订单查询意图
                   - 当用户提及"商品"、"查找"、"搜索"、"手机"、"衣服"、"价格"、"库存"等关键词时，识别为商品查询意图
                   2. **服务调用**：
                   - 订单查询意图 → 调用 `getOrderQueryService` 方法
                   - 商品查询意图 → 调用 `getGoodsQueryService` 方法
                   请确保准确识别用户意图并正确调用相应服务，提取所有可用参数填充到函数调用中，用户ID从prompt获取。
                   仅当用户明确要求其他操作或工具无法处理时，才直接回复。
                   当前用户的id: {user_id}
                 """;

            String content = chatClient
                    .prompt()
                    .system(resolvedPrompt)
                    .user(userMessage)
                    .call()
                    .content();
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

    /**
     * 检查文本是否包含敏感词
     * @param text 文本
     * @return 错误信息，如果没有错误返回null
     */
    private String check(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "内容不能为空";
        }
        if (sensitiveWordBs.contains(text)) {
            return "内容包含敏感词汇，这个问题我暂时无法回答，让我们换个话题再聊聊吧！";
        }
        return null;
    }
}