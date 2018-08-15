package com.cdx.controller;

import com.cdx.common.domain.EasyUITreeNode;
import com.cdx.common.domain.WebMallResponse;
import com.cdx.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类管理Controller
 */
@Controller
public class ContentCategoryController {

    /**
     * 注入内容管理Service
     */
    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 调用服务层的内容分类列表Service，显示内容分类的树形图
     * @param parentId 从前端页面获取当前节点的id，默认值为0，作为其子节点的父节点
     * @return 返回封装好的EasyUITreeNode类
     */
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getContentCategoryList(@RequestParam(name = "id", defaultValue = "0")Long parentId) {
        return contentCategoryService.getContentCategoryList(parentId);
    }

    /**
     * 新增内容分类树的节点
     * @param parentId 新增节点的父节点，从request中通过名字进行参数绑定
     * @param name 新增节点的名字，从request中通过名字进行参数绑定
     * @return 返回WebMallResponse类型的类，转换成Json数据传递到前端页面
     */
    @RequestMapping("/content/category/create")
    @ResponseBody
    public WebMallResponse addContentCategory(Long parentId, String name) {
        return contentCategoryService.addContentCategory(parentId, name);
    }

    /**
     * 修改当前节点的名字
     * @param id 当前节点Id，通过名称进行参数绑定
     * @param name 修改后的节点名字，通过名称进行参数绑定
     * @return 返回WebMallResponse类型的类，转换成Json数据传递到前端页面
     */
    @RequestMapping("/content/category/update")
    @ResponseBody
    public WebMallResponse updateContentCategory(Long id, String name) {
        return contentCategoryService.updateContentCategory(id, name);
    }

    /**
     * 删除当前节点
     * @param id 当前节点的id
     */
    @RequestMapping("/content/category/delete/")
    @ResponseBody
    public void deleteContentCategory(Long id) {
        contentCategoryService.deleteContentCategory(id);
    }
}
