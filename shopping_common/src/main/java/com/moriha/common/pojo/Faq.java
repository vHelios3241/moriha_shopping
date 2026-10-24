package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/*
 * FAQ实体类
 */
@Data
public class Faq {
    /**
     * 主键ID：使用字符串存储UUID
     * 策略：使用程序生成UUID，INSERT时自动填充
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 分类ID
     * 1-订单问题, 2-支付问题, 3-商品问题, 4-账户问题, 5-其他问题
     */
    private Integer categoryId;

    /**
     * 问题内容
     */
    private String question;

    /**
     * 答案内容
     */
    private String answer;

    /**
     * 状态
     * 0-禁用, 1-启用
     */
    private Integer status;

    /**
     * 使用次数
     * 记录该FAQ被匹配使用的次数
     */
    private Integer useCount;
}
