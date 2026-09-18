package com.moriha.common.result;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MyBlockExceptionHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        if(e instanceof FlowException ){
            BaseResult result = new BaseResult<>(701, "流量异常", null);
            httpServletResponse.setContentType("text/json, charset = utf-8");
            httpServletResponse.getWriter().write(JSON.toJSONString(result));
        }
    }
}
