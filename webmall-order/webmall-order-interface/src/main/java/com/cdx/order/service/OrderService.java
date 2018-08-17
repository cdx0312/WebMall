package com.cdx.order.service;

import com.cdx.common.domain.WebMallResponse;
import com.cdx.order.domain.OrderInfo;

/**
 * 订单服务接口
 * Created by cdx0312
 */
public interface OrderService {
    /**
     * 创建订单接口
     * @param orderInfo 订单信息
     * @return 订单Id
     */
    WebMallResponse createOrder(OrderInfo orderInfo);
}
