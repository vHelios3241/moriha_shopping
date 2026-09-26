package com.moriha.shopping_webhooks.controller;
/*
 * 告警控制器
 */

import com.moriha.shopping_webhooks.dto.AlarmMessageDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WebHooksController {
    /**
     * SkyWalking告警的回调方法，也就是该接口是暴露给SkyWalking的，SkyWalking告警时会调用该接口，
     * 我们接受到SkyWalking传来的告警信息，可以处理告警信息
     * @param alarmMessageDtoList SkyWalking封装的告警信息
     */
    @PostMapping("/alarm")
    public void alarm(@RequestBody List<AlarmMessageDto> alarmMessageDtoList){
        StringBuilder sb = new StringBuilder();
        alarmMessageDtoList.forEach(info -> {
                    sb.append("\nscope:").append(info.getScopeId())
                            .append("\nScope实体:").append(info.getScope())
                            .append("\n告警信息:").append(info.getAlarmMessage())
                            .append("\n告警规则:").append(info.getRuleName())
                            .append("\n\n------------------------\\n\\n");
                });
        System.out.println(sb.toString());
    }
}
