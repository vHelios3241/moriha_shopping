package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付记录
 */
@Data
public class Payment implements Serializable {
    // 支付记录id
    @TableId
    private Long id;

    // 订单Id
    private String orderId;

    // 支付系统交易编号
    private String transactionId;

    // 交易类型
    private String tradeType;

    // 交易状态
    private String tradeState;

    // 支付金额（元）
    private BigDecimal payerTotal;

    // 通知参数
    private String content;

    // 创建时间
    private Date createTime;

}
