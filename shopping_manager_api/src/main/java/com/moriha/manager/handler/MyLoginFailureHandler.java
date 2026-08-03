package com.moriha.manager.handler;

import com.alibaba.fastjson2.JSON;
import com.moriha.common.result.BaseResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

// 登录失败处理器
public class MyLoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        BaseResult result = new BaseResult<>(402, "用户名或密码错误", null);
        response.setContentType("text/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
}
