package com.cdx.search.controller;

import com.cdx.common.domain.SearchResult;
import com.cdx.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 搜索服务Controller
 * Created by cdx0312
 * 2018/3/8
 */
@Controller
public class SearchController {

    /**
     * 注入SearchService
     */
    @Autowired
    private SearchService searchService;

    /**
     * 注入每页显示的记录数
     */
    @Value("${SEARCH_RESULT_ROWS}")
    private Integer SEARCH_RESULT_ROWS;

    @RequestMapping("/search")
    public String search(@RequestParam("q")String queryString,
                         @RequestParam(defaultValue = "1")Integer page, Model model) {
        //调用服务执行查询
        try {
            //查询条件进行转码，解决get请求乱码问题
            queryString = new String(queryString.getBytes("iso8859-1"), "utf-8");
            SearchResult searchResult = searchService.search(queryString, page, SEARCH_RESULT_ROWS);
            //把结果传递给页面
            model.addAttribute("query", queryString);
            model.addAttribute("totalPages", searchResult.getTotalPages());
            model.addAttribute("itemList", searchResult.getItemList());
            model.addAttribute("page", page);
            model.addAttribute("totalPages", searchResult.getTotalPages());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回逻辑视图
        return "search";
    }
}
