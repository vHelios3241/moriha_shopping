package com.moriha.shopping_pay_service.service;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.moriha.common.pojo.Orders;
import com.moriha.common.pojo.Payment;
import com.moriha.common.result.BusException;
import com.moriha.common.result.CodeEnum;
import com.moriha.common.service.ZfbPayService;
import com.moriha.shopping_pay_service.ZfbPayConfig;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@DubboService
public class ZfbPayServiceImpl implements ZfbPayService {

    @Autowired
    private ZfbPayConfig zfbPayConfig;
    @Autowired
    private AlipayClient alipayClient;

    /*
     * 生成二维码
     * @param orders 订单对象
     * @return 二维码字符串
     */
    @Override
    public String pcPay(Orders orders) {
        // 1. 创建请求对象
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        // 2. 设置请求内容
        request.setNotifyUrl(zfbPayConfig.getNotifyUrl() + zfbPayConfig.getPcNotify());
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orders.getId()); // 订单编号
        bizContent.put("total_amount", orders.getPayment()); // 订单金额
        bizContent.put("subject", orders.getCartGoods().get(0).getGoodsName());
        request.setBizContent(bizContent.toJSONString());
        // 3. 发送请求
        try {
            AlipayTradePrecreateResponse response =  alipayClient.execute(request);
            // 4. 返回二维码
            return response.getQrCode();
        } catch (AlipayApiException e) {
            throw new BusException(CodeEnum.QR_CODE_ERROR);
        }
    }

    @Override
    public void checkSign(Map<String, Object> paramMap) {

    }

    @Override
    public void addPayment(Payment payment) {

    }
}
