package com.moriha.manager.handler;

import com.alibaba.fastjson2.JSON;
import com.moriha.common.result.BaseResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

// 登录成功处理器
public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        BaseResult result = new BaseResult<>(200, "登录成功", null);
        // 设置响应内容类型为 JSON
        response.setContentType("text/json;charset=UTF-8");
        // 返回操作成功的 JSON 响应
        response.getWriter().write(JSON.toJSONString(result));
    }
}
