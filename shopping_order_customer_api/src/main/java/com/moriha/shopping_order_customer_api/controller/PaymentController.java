package com.moriha.shopping_order_customer_api.controller;

import com.alibaba.fastjson2.JSON;
import com.moriha.common.pojo.Orders;
import com.moriha.common.pojo.Payment;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.OrdersService;
import com.moriha.common.service.ZfbPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/user/payment")
@RestController
public class PaymentController {

    @DubboReference
    private OrdersService ordersService;
    @DubboReference
    private ZfbPayService zfbPayService;

    /*
     *  生成二维码
     * @param orderId 订单id
     * @return 二维码字符串
     */
    @PostMapping("/pcPay")
    public BaseResult<String> pcPay(String orderId){
        Orders order = ordersService.findById(orderId);
        String codeurl = zfbPayService.pcPay(order);
        return BaseResult.ok(codeurl);
    }

    /*
     * 支付成功回调方法
     */
    @PostMapping("/success/notify")
    public BaseResult successNotify(HttpServletRequest request){
        // 1.验签
        Map<String,Object> paramMap = new HashMap();
        paramMap.put("requestParameterMap",request.getParameterMap());
        zfbPayService.checkSign(paramMap);
        // 拿到订单状态和编号
        String trade_status = request.getParameter("trade_status");  // 交易状态
        String out_trade_no = request.getParameter("out_trade_no");  // 订单编号
        // 如果交易成功
        if(trade_status.equals("TRADE_SUCCESS")){
            // 2. 修改订单状态
            Orders orders = ordersService.findById(out_trade_no);
            orders.setStatus(2); // 订单状态为已支付
            orders.setPaymentType(2); // 支付宝支付
            orders.setCreateTime(new Date()); // 设置支付时间
            ordersService.update(orders); // 修改订单状态
            // 3.添加交易记录
            Payment payment = new Payment();
            payment.setOrderId(out_trade_no); // 订单编号
            payment.setTransactionId(out_trade_no); //交易编号
            payment.setTradeType("支付宝支付"); //交易类型
            payment.setTradeState(trade_status); //交易状态
            payment.setPayerTotal(orders.getPayment()); //付款数
            payment.setContent(JSON.toJSONString(request.getParameterMap()));  // 支付详情
            payment.setCreateTime(new Date()); // 支付时间
            zfbPayService.addPayment(payment);
        }
        return BaseResult.ok();
    }

}
