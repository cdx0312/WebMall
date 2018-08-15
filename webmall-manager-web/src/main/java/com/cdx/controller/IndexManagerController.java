package com.cdx.controller;

/**
 * Created by cdx0312
 * 2018/3/8
 */

import com.cdx.common.domain.WebMallResponse;
import com.cdx.search.service.SearchItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 索引库维护Controller
 */
@Controller
public class IndexManagerController {

    /**
     * 注入搜索服务
     */
    @Autowired
    private SearchItemService searchItemService;

    /**
     * 将数据库的商品信息导入到索引库中
     * @return
     */
    @RequestMapping("/index/import")
    @ResponseBody
    public WebMallResponse importIndex() {
        return searchItemService.importItemToIndex();
    }
}
