package com.moriha.shopping_custcare_service.functioncall;

import com.moriha.common.pojo.Orders;
import com.moriha.common.service.OrdersService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

/*
 * 订单查询服务
 */
@Component  // 表示这是一个Spring组件，可以被Spring容器管理
public class OrderQueryService implements Function<OrderQueryService.Request, OrderQueryService.Response>{

    @DubboReference
    private OrdersService ordersService;

    /**
     * 应用
     * @param request 请求参数
     * @return 响应参数
     */
    @Override
    public Response apply(Request request) {
        Long userId = request.userId();
        Integer size = request.size() == null ? 1 : request.size();
        Integer status = request.status();
        List<Orders> orders = ordersService.findUserOrders(userId,status);
        List<Orders> subList = orders.subList(0, size < orders.size() ? size : orders.size());
        return new Response(subList);
    }

    /**
     * 输入
     * @param userId 用户ID
     * @param size 查询条数
     * @param status 订单状态
     */
    public record Request(Long userId, Integer size, Integer status){}

    /**
     * 输出
     * @param orders 订单信息集合
     */
    public record Response(List<Orders> orders){}}
