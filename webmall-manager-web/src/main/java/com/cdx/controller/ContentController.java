package com.cdx.controller;

import com.cdx.common.domain.EasyUIDataGridResult;
import com.cdx.common.domain.WebMallResponse;
import com.cdx.content.service.ContentService;
import com.cdx.domain.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 内容管理Controller
 */
@Controller
public class ContentController {
    /**
     * 通过Dubbo获取content中的内容服务并将其注入到Controller中
     */
    @Autowired
    private ContentService contentService;

    /**
     * 分页展示内容信息
     * @param page 当前页数 参数从Request中通过名称进行绑定
     * @param rows 每页的行数 参数从Request中通过名称进行绑定
     * @param categoryId 展示内容拥有的共同类别id 参数从Request中通过名称进行绑定
     * @return 返回EasyUIDataGridResult类转换成JSon字符串的值传回
     * 其中该类包含两个参数，total表示商品总数，rows表示当前商品的列表
     * 其名称时根据前段页面来确定。
     */
    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGridResult getContentList(Integer page, Integer rows, Long categoryId) {
        return contentService.getContentList(page, rows, categoryId);
    }

    /**
     * 新增内容
     * @param content 从前段页面接收各个属性的参数，并封装到content类中
     * @return 返回WebMallResponse封装的数据
     */
    @RequestMapping("/content/save")
    @ResponseBody
    public WebMallResponse addContent(TbContent content) {
        return contentService.addContent(content);
    }

    /**
     * 修改内容
     * @param content 从页面接收各个属性的参数，并封装到content类中
     * @return 返回WebMallResponse封装的数据
     */
    @RequestMapping("/rest/content/edit")
    @ResponseBody
    public WebMallResponse editContent(TbContent content) {
        return contentService.editContent(content);
    }

    /**
     * 删除内容
     * @param ids 存储了id值的字符串，id之间为， 分隔
     * @return 返回WebMallResponse封装的数据
     */
    @RequestMapping("/content/delete")
    @ResponseBody
    public WebMallResponse deleteContentByIds(String ids) {
        return contentService.deleteContentByIds(ids);
    }
}
