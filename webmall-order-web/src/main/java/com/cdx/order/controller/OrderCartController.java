package com.cdx.order.controller;

import com.cdx.common.domain.WebMallResponse;
import com.cdx.common.util.CookieUtils;
import com.cdx.common.util.JsonUtils;
import com.cdx.domain.TbItem;
import com.cdx.domain.TbUser;
import com.cdx.order.domain.OrderInfo;
import com.cdx.order.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 订单确认页面处理Controller
 * Created by cdx0312
 */
@Controller
public class OrderCartController {

    /**
     * 购物车在Cookie中保存的key
     */
    @Value("${CART_KEY}")
    private String CART_KEY;

    /**
     * 注入订单服务
     */
    @Autowired
    private OrderService orderService;

    /**
     * 展示订单确认页面
     * @param request
     * @return
     */
    @RequestMapping("/com/cdx/order/order-cart")
    public String showOrder(HttpServletRequest request) {
        //用户必须是登录状态
        //取用户Id,根据用户信息取收货地址列表
        TbUser user = (TbUser) request.getAttribute("user");
//        System.out.println(user.getUsername());
        //把收货地址列表展示到页面上
        //从Cookie中取购物车商品列表展示到页面
        List<TbItem> cartItemList = getCartItemList(request);
        request.setAttribute("cartList", cartItemList);
        //返回逻辑视图
        return "order-cart";
    }

    /**
     * 从Cookie中取商品列表0
     * @param request
     * @return
     */
    private List<TbItem> getCartItemList(HttpServletRequest request) {
        String json = CookieUtils.getCookieValue(request, CART_KEY, true);
        if (StringUtils.isBlank(json)) {
            return null;
        }
        return JsonUtils.jsonToList(json, TbItem.class);
    }

    /**
     * 生成订单的Controller
     */
    @RequestMapping(value = "/com/cdx/order/create", method = RequestMethod.POST)
    public String createOrder(OrderInfo orderInfo, Model model) {
        //生成订单
        WebMallResponse result = orderService.createOrder(orderInfo);
        //返回逻辑视图
        model.addAttribute("orderId", orderInfo.getOrderId());
        model.addAttribute("payment", orderInfo.getPayment());
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(3);
        model.addAttribute("data", dateTime.toString("yyyy-MM-dd"));
        return "success";
    }
}
