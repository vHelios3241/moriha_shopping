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
import com.moriha.shopping_pay_service.mapper.PaymentMapper;
import com.moriha.shopping_pay_service.util.ZfbVerifierUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@DubboService
public class ZfbPayServiceImpl implements ZfbPayService {

    @Autowired
    private ZfbPayConfig zfbPayConfig;
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private PaymentMapper paymentMapper;

    /*
     * 生成二维码
     */
    @Override
    public String pcPay(Orders orders) {
        // 判断订单状态（未支付→二维码）
        if(orders.getStatus() != 1){
            throw new BusException(CodeEnum.ORDER_STATUS_ERROR);
        }
        // 1. 创建请求对象
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        // 2. 设置请求内容
        request.setNotifyUrl(zfbPayConfig.getNotifyUrl() + zfbPayConfig.getPcNotify());
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orders.getId()); // 订单编号
        bizContent.put("total_amount", orders.getPayment()); // 订单金额
        bizContent.put("subject", orders.getCartGoods().get(0).getGoodsName()); // 订单标题
        request.setBizContent(bizContent.toJSONString());
        // 3. 发送请求
        try {
            AlipayTradePrecreateResponse response =  alipayClient.execute(request);
//            System.out.println("支付宝响应: " + response.getBody());
            // 4. 返回二维码
            return response.getQrCode();
        } catch (AlipayApiException e) {
//            e.printStackTrace();
            throw new BusException(CodeEnum.QR_CODE_ERROR);
        }
    }

    /*
     * 验签
     */
    @Override
    public void checkSign(Map<String, Object> paramMap) {
        // 1. 获取所有参数
        Map<String, String[]> requestParameterMap = (Map<String, String[]>) paramMap.get("requestParameterMap");
        // 2. 验签
        boolean vaild = ZfbVerifierUtils.isValid(requestParameterMap, zfbPayConfig.getPublicKey());
        // 3. 验签失败，抛出异常
        if(!vaild){
            throw new BusException(CodeEnum.CHECK_SIGN_ERROR);
        }
    }

    /*
     * 生成交易记录
     */
    @Override
    public void addPayment(Payment payment) {
        paymentMapper.insert(payment);
    }
}
