package com.cdx.content.controller;

import com.cdx.common.util.JsonUtils;
import com.cdx.content.domain.AD1Node;
import com.cdx.content.service.ContentService;
import com.cdx.domain.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页展示Controller
 */
@Controller
public class IndexController {

    /**
     * 注入内容服务
     */
    @Autowired
    private ContentService contentService;

    /**
     * 从resource.properties文件中注入AD1Node的属性值
     */
    @Value("${AD1_CATEGORY_ID}")
    private Long AD1_CATEGORY_ID;
    @Value("${AD1_WIDTH}")
    private Integer AD1_WIDTH;
    @Value("${AD1_WIDTH_B}")
    private Integer AD1_WIDTH_B;
    @Value("${AD1_HEIGHT}")
    private Integer AD1_HEIGHT;
    @Value("${AD1_HEIGHT_B}")
    private Integer AD1_HEIGHT_B;

    /**
     * 商城首页展示功能
     * 启动Tomcat时，会去找web.xml中定义的index.html,
     * 从而进入本方法的Contoller,最终返回index.jsp页面
     * @return 返回String经过springmvc中的前缀和后缀定位到index.jsp文件
     */
    @RequestMapping("/index")
    public String showIndex(Model model) {
        //根据cid查询轮播图内容列表
        List<TbContent> list = contentService.getContentByCid(AD1_CATEGORY_ID);
        //将TBContent列表转换成AD1Node列表
        List<AD1Node> ad1Nodes = new ArrayList<>();
        for (TbContent tbContent : list) {
            AD1Node node = new AD1Node();
            node.setAlt(tbContent.getTitle());
            node.setHeight(AD1_HEIGHT);
            node.setHegihtB(AD1_HEIGHT_B);
            node.setWidth(AD1_WIDTH);
            node.setWidthB(AD1_WIDTH_B);
            node.setSrc(tbContent.getPic());
            node.setSrcB(tbContent.getPic2());
            node.setHref(tbContent.getUrl());
            //添加到节点列表
            ad1Nodes.add(node);
        }
        //将AD1Node列表转换成JSON数据
        String ad1Json = JsonUtils.objectToJson(ad1Nodes);
        //将JSOn数据传递给页面
        model.addAttribute("ad1", ad1Json);

        return "index";
    }
}
