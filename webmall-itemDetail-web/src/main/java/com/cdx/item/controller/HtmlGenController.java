package com.cdx.item.controller;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * FreeMarker测试Controller
 * Created by cdx0312
 * 2018/3/11
 */
@Controller
public class HtmlGenController {
    /**
     * 注入Configure
     */
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @RequestMapping("/genhtml")
    @ResponseBody
    public String genHtml() throws IOException, TemplateException {
        //生成静态页面并返回结果
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("hello.ftl");
        Map data = new HashMap();
        data.put("hello", "spring freemarker test");
        Writer out = new FileWriter(new File("webmall-itemDetail-web/src/main/webapp/WEB-INF/freemarkerOutFile/test.html"));
        template.process(data, out);
        out.close();
        return "OK";
    }


}
