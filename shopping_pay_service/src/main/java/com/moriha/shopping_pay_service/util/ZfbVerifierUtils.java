package com.moriha.shopping_pay_service.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

import java.util.HashMap;
import java.util.Map;

/*
 * 支付验签工具
 */
public class ZfbVerifierUtils {
  /*
   * 验证支付宝异步通知签名合法性
   *
   * @param requestParameterMap 支付宝请求的所有参数
   * @param alipayPublicKey   支付宝公钥
   * @return
   */
  public static boolean isValid(Map<String, String[]> requestParameterMap, String alipayPublicKey) {
    // 1. 获取支付宝post发送过来的信息
    Map<String, String> resultMap = new HashMap<>();
    for (Object v : requestParameterMap.entrySet()) {
      Map.Entry<String, String[]> item = (Map.Entry<String, String[]>) v;
      resultMap.put(item.getKey(), item.getValue()[0]);
     }
    try {
      return AlipaySignature.rsaCheckV1(resultMap, alipayPublicKey, "UTF-8", "RSA2");
     } catch (AlipayApiException e) {
      e.printStackTrace();
     }
    return false;
   }
}