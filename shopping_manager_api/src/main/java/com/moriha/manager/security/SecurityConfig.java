package com.moriha.manager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// security配置类
@Configuration
public class SecurityConfig {
    // Spring Security配置
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 自定义表单登录
        http.formLogin(
                form -> {
                    form.usernameParameter("username") // 用户名项
                            .passwordParameter("password") // 密码项
                            .loginProcessingUrl("/admin/login") // 登录提交路径
                            .successHandler(new MyLoginSuccessHandler()) // 登录成功处理器
                            .failureHandler(new MyLoginFailureHandler()); // 登录失败处理器
                }
        );


        // 权限拦截配置
        http.authorizeHttpRequests(
                resp -> {
                    resp.requestMatchers("/login", "/admin/login").permitAll(); // 登录请求不需要认证
                    resp.anyRequest().authenticated();// 其余请求都需要认证
                }
        );


        // 退出登录配置
        http.logout(
                logout -> {
                    logout.logoutUrl("/admin/logout") // 注销的路径
                            .logoutSuccessHandler(new MyLogoutSuccessHandler()) // 登出成功处理器
                            .clearAuthentication(true) // 清除认证数据
                            .invalidateHttpSession(true); // 清除session
                }
        );


        // 异常处理
        http.exceptionHandling(
                exception -> {
                    exception.authenticationEntryPoint(new MyAuthenticationEntryPoint())// 未登录处理器
                            .accessDeniedHandler(new MyAccessDeniedHandler()); // 权限不足处理器
                }
        );


        // 跨域访问
        http.cors();

        // 关闭csrf防护
        http.csrf(csrf ->{
            csrf.disable();
        });


        return http.build();
    }


    // 加密工具
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

