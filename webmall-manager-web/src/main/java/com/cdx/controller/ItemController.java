package com.cdx.controller;

import com.cdx.common.domain.EasyUIDataGridResult;
import com.cdx.common.domain.WebMallResponse;
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

    /**
     * 分页展示商品信息
     * @param page 当前页数，参数从Request中通过名称进行绑定
     * @param rows 每页的行数，参数从Request中通过名称进行绑定
     * @return 返回EasyUIDataGridResult类转换成JSon字符串的值传回
     * 其中该类包含两个参数，total表示商品总数，rows表示当前商品的列表
     * 其名称时根据前段页面来确定。
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult getItemList(Integer page, Integer rows) {
        return itemService.getItemList(page, rows);
    }

    /**
     * 商品添加功能，表单提交的信息会自动绑定到Item的同名属性中，多余的desc属性将一同从页面传递
     * 到Controller，调用service层的itemService的addItem方法，分别将商品信息和商品描述信息添加
     * 到相应的数据库表中
     * @param item 其中封装了表单提交过来的商品信息
     * @param desc 商品描述信息，会存到商品描述类的属性中
     * @return 返回WebMallResponse类，其中封装了三个属性。
     */
    @RequestMapping(value = "/item/save", method = RequestMethod.POST)
    @ResponseBody
    public WebMallResponse addItem(TbItem item, String desc) {
        return itemService.addItem(item, desc);
    }

}
