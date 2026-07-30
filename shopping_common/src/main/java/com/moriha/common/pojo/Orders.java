package com.moriha.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单
 */
@Data
public class Orders implements Serializable{
    @TableId(type = IdType.ASSIGN_ID)
    private String id; // 订单编号,后台自动生成
    @TableField(exist = false) // 数据库不存在该字段
    private List<CartGoods> cartGoods; // 购物车商品集合
    private BigDecimal payment; // 支付金额
    private Integer paymentType; // 支付方式  1、微信支付   2、支付宝支付
    private BigDecimal postFee; // 邮费
    private Integer status; // 订单状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
    private Date createTime; // 订单创建时间
    private Date paymentTime; // 付款时间
    private Date consignTime; // 发货时间
    private Date endTime; // 交易完成时间
    private Date closeTime; // 交易关闭时间
    private String shippingName; // 物流名称
    private String shippingCode; // 物流单号
    private Long userId; // 用户id
    private String buyerMessage; // 买家留言
    private String buyerNick; // 买家昵称
    private String receiverAreaName; // 收货地址
    private String receiverMobile; // 收货手机号
    private String receiverZipCode; // 收货邮编
    private String receiver; // 收货人
    private Date expire; // 订单过期时间
}
