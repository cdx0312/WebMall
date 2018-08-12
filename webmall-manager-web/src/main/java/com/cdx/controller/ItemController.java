package com.cdx.controller;

import com.cdx.domain.TbItem;
import com.cdx.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品管理Controller
 */
@Controller
public class ItemController {

    /**
     * 通过Dubbo获取taotao-manager工程中的商品管理服务，itemService
     * 将其注入到Controller中
     */
    @Autowired
    private ItemService itemService;

    /**
     * 通过Resuful标准向后台传输itemId,并根据其查找相应的商品信息
     * @param itemId 传入的itemId，方法中的参数名和其一致，不需要指定
     * @return 返回对应主键的商品类，查看其JSON数据
     */
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {
        TbItem tbItem = itemService.getItemById(itemId);
        return tbItem;
    }
}
