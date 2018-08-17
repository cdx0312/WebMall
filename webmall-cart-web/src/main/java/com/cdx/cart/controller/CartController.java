package com.cdx.cart.controller;

import com.cdx.common.domain.WebMallResponse;
import com.cdx.common.util.CookieUtils;
import com.cdx.common.util.JsonUtils;
import com.cdx.domain.TbItem;
import com.cdx.jedis.JedisClient;
import com.cdx.service.ItemService;
import com.cdx.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 购物车管理Controller
 * Created by cdx0312
 */
@Controller
public class CartController {

    /**
     * 注入商品服务
     */
    @Autowired
    private ItemService itemService;

    /**
     * 购物车在Cookie中保存的key
     */
    @Value("${CART_KEY}")
    private String CART_KEY;

    @Value("${CART_EXPIRE}")
    private Integer CART_EXPIRE;

    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    /**
     * 注入用户登录服务
     */
    @Autowired
    private UserService userService;

    /**
     * 注入redis客户端
     */
    @Autowired
    private JedisClient jedisClient;

    /**
     * 向购物车中添加商品
     * @param itemId 要添加的商品id
     * @param num 商品数量
     * @param request
     * @param response
     * @return 跳转逻辑视图
     */
    @RequestMapping("/cart/add/{itemId}")
    public String addItem(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
                          HttpServletRequest request, HttpServletResponse response) {
        //取购物城中商品列表
        List<TbItem> cartItemList = getCartItemList(request);
        //判断当前购物城里面是否已经存在要添加的商品，
        boolean flag = false;
        if (cartItemList != null && cartItemList.size() > 0) {
            for (TbItem item: cartItemList) {
                if (item.getId() == itemId.longValue()) {
                    //存在则数量添加
                    item.setNum(item.getNum() + num);
                    flag = true;
                    break;
                }
            }
        }
        //不存在则添加一个新的商品
        if (!flag) {
            //调用服务获取商品信息
            TbItem itemById = itemService.getItemById(itemId);
            //设置购买的商品数量
            itemById.setNum(num);
            //取一张图片
            String image = itemById.getImage();
            if (StringUtils.isNotBlank(image)) {
                String[] images = image.split(",");
                itemById.setImage(images[0]);
            }
            //商品添加到购物车
            if (cartItemList == null) {
                cartItemList = new ArrayList<TbItem>();
            }
            cartItemList.add(itemById);
        }
        //将购物城列表写入cookie
        CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), CART_EXPIRE, true);
        //返回添加成功页面
        return "cartSuccess";
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
     * 从Cookie中取购物车列表，传递给JSP，展示视图
     * @param request
     * @return
     */
    @RequestMapping("/cart/cart")
    public String showCatList(HttpServletRequest request, HttpServletResponse response) {
        //TODO: redis和Cookie结合来解决Cookie容量不够和不同机器登录同步购物车的功能
        //1、判断用户是否登陆，如果未登录，购物车展示数据为Cookie中保存的，如果已经登录，去redis中去取数据展示
        String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
        //根据token从数据库中获取用户的信息
        WebMallResponse result = userService.getUserByToken(token);
        //如果token不存在或者没有取到用户，则Result的status为400，此时说明未登录，则返回Cookie中的购物车数据
        if (result.getStatus() != 200) {
            List<TbItem> cartItemList = getCartItemList(request);
            request.setAttribute("cartList", cartItemList);
            return "cart";
        }
        //如果取到了用户，说明已经登录，
        //a.则需要将Cookie中的数据写入到redis中
        List<TbItem> cartItemListFromCookie = getCartItemList(request);
        //校验Cookie中取到的是否为空：
        if (cartItemListFromCookie != null && cartItemListFromCookie.size() > 0) {
            //从redis中获取存在的购物车数据
            Jedis jedis = jedisClient.getJedis();
            Map<String, String> map = jedis.hgetAll(token);
            List<TbItem> cartItemListFromRedis = new ArrayList<>();
            for (String string : map.keySet()) {
                cartItemListFromRedis.add(JsonUtils.jsonToPojo(map.get(string), TbItem.class));
            }
            for (TbItem tbItem : cartItemListFromCookie) {
                //如果购物车商品已经存在
                if (map.keySet().contains(tbItem.getId().toString())) {
                    TbItem item = JsonUtils.jsonToPojo(map.get(tbItem.getId().toString()), TbItem.class);
                    item.setNum(item.getNum() + tbItem.getNum());
                    jedis.hset(token, tbItem.getId().toString(), JsonUtils.objectToJson(item));
                }
                jedisClient.hset(token, tbItem.getId().toString(), JsonUtils.objectToJson(tbItem));
            }
            //b.把Cookie中数据删除
            CookieUtils.deleteCookie(request, response, CART_KEY);
        }
        //c.展示购物车列表时以redis为准
        Jedis jedis = jedisClient.getJedis();
        Map<String, String> map = jedis.hgetAll(token);

        //d.如果redis中有的数据，Cookie中也有，相同商品时数量相加
        List<TbItem> cartItemListFromRedis = new ArrayList<>();
        for (String string : map.keySet()) {
            cartItemListFromRedis.add(JsonUtils.jsonToPojo(map.get(string), TbItem.class));
        }
        request.setAttribute("cartList", cartItemListFromRedis);
        return "cart";
    }

    /**
     * 从Cookie中取购物车列表，查询到对应的商品，更新商品数量，返回成功
     * @param itemId 商品Id
     * @param num 数量
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public WebMallResponse updateItemNum(@PathVariable Long itemId, @PathVariable Integer num,
                                      HttpServletRequest request, HttpServletResponse response) {
        List<TbItem> cartItemList = getCartItemList(request);
        assert cartItemList != null;
        for (TbItem tbItem : cartItemList) {
            if (tbItem.getId() == itemId.longValue()) {
                tbItem.setNum(num);
                break;
            }
        }
        CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), CART_EXPIRE, true);
        return WebMallResponse.ok();
    }

    /**
     * 在购物车中删除商品
     * @param itemId 商品ID
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request,
                                 HttpServletResponse response) {
        //从Cookie中取购物车列表
        List<TbItem> cartItemList = getCartItemList(request);
        //找到对应的商品并删除
        for (TbItem tbItem : cartItemList) {
            if (tbItem.getId() == itemId.longValue()) {
                cartItemList.remove(tbItem);
                break;
            }
        }
        //将删除之后的列表写入Cookie
        CookieUtils.setCookie(request, response, CART_KEY, JsonUtils.objectToJson(cartItemList), CART_EXPIRE, true);
        //重定向购物车列表页面
        return "redirect:/cart/cart.html";
    }
}
