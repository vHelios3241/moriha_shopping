package com.moriha.shopping_custcare_service.Config;

import com.moriha.shopping_custcare_service.functioncall.GoodsQueryService;
import com.moriha.shopping_custcare_service.functioncall.OrderQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

@Configuration
public class FunctionCallConfig {

    /**
     * 注册订单查询服务
     * @return 订单查询服务函数
     */
    @Bean
    @Description("查询当前用户的订单信息，输入订单状态（1=待支付，2=已完成）和查询条数（默认为1），用户ID从prompt获取")
    public Function<OrderQueryService.Request,OrderQueryService.Response> getOrderQueryService(){
        return new OrderQueryService();
    }

    /**
     * 注册商品查询服务
     * @return 商品查询服务函数
     */
    @Bean
    @Description("查询商品信息，输入商品关键字和查询条数（默认为1）")
    public Function<GoodsQueryService.Request, GoodsQueryService.Response> getGoodsQueryService() {
        return new GoodsQueryService();
    }
}
