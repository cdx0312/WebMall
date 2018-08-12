package com.cdx.controller;

import com.cdx.common.domain.EasyUITreeNode;
import com.cdx.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商品分类管理Controller
 */
@Controller
public class ItemCatController {
    /**
     * 通过Dubbo获取taotao-manager的商品分类管理服务，
     * 并将其注入到Controller中
     */
    @Autowired
    private ItemCatService itemCatService;

    /**
     * 根据请求来展示当前节点作为父节点的子节点的树形结构
     * 主要是根据EasyUI前段来修改的
     * @param parentId 选中节点的Id，由于其要作为其子节点的父节点，需要进行参数绑定
     * @return 返回EasyUITreeNode,来实现树形分类的展示
     */
    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCatList(@RequestParam(name = "id", defaultValue = "0")Long parentId) {
        return itemCatService.getItemCat(parentId);
    }
}
