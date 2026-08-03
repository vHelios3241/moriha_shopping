package com.moriha.manager.handler;

import com.alibaba.fastjson2.JSON;
import com.moriha.common.result.BaseResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

// 登出成功处理器
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        BaseResult result = new BaseResult<>(200, "注销成功", null);
        response.setContentType("text/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(result));
    }
}
