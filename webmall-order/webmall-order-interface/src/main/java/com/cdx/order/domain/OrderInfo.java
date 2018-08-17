package com.cdx.order.domain;


import com.cdx.domain.TbOrder;
import com.cdx.domain.TbOrderItem;
import com.cdx.domain.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

/**
 * 接收提交订单时提交的表单数据
 * Created by cdx0312
 */
public class OrderInfo extends TbOrder implements Serializable{
    private List<TbOrderItem> orderItems;
    private TbOrderShipping orderShipping;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
