package com.cdx.item.controller;

import com.cdx.domain.TbItem;
import com.cdx.domain.TbItemDesc;
import com.cdx.item.pojo.Item;
import com.cdx.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 商品详情页面展示Controller
 * Created by cdx0312
 * 2018/3/9
 */
@Controller
public class ItemController {

    /**
     * 注入商品服务
     */
    @Autowired
    private ItemService itemService;

    /**
     * 根据传入的商品ID，展示相应的商品信息
     * @param itemId
     * @return
     */
    @RequestMapping("/item/{itemId}")
    public String showItem(@PathVariable Long itemId, Model model) {
        //取商品基本信息
        TbItem tbItem = itemService.getItemById(itemId);
        Item item = new Item(tbItem);
        //取商品详情
        TbItemDesc itemDesc = itemService.getItemDescById(itemId);
        model.addAttribute("item", item);
        model.addAttribute("itemDesc", itemDesc);
        return "item";
    }
}
