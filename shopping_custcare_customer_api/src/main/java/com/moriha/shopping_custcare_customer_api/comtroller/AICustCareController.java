package com.moriha.shopping_custcare_customer_api.comtroller;

import com.moriha.common.result.BaseResult;
import com.moriha.common.service.AICustCareService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/custcare")
public class AICustCareController {

    @DubboReference(timeout = 30000) // 指定30秒超时
    private AICustCareService aiCustCareService;

    /*
     * 回答用户问题
     * @param message 问题
     * @return 答案
     */
    @GetMapping("/answer")
    public BaseResult<String> answer(@RequestParam("message") String message){
        String response = aiCustCareService.generateResponse(message);
        return BaseResult.ok(response);
    }
}
