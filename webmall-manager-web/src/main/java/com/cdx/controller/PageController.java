package com.cdx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面展示controller
 */
@Controller
public class PageController {

    /**
     * 输入地址为：localhost:8081/时，跳转到index.jsp页面
     * @return
     */
    @RequestMapping("/")
    public String showIndex() {
        return "index";
    }

    /**
     * 根据输入的地址，跳转到对应的jsp页面
     * 比如:输入item-add，跳转到item-add.jsp页面
     * @param page 输入的地址名
     * @return 返回给试图解析器的字符串，加上对应的前缀和后缀，跳转到相应的页面
     */
    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page) {
        return page;
    }
}
