package com.cdx.search.service;


import com.cdx.common.domain.SearchResult;

/**
 * 搜索服务
 * Created by cdx0312
 * 2018/3/8
 */
public interface SearchService {
    /**
     * 查询索引库，返回结果
     * @param queryString 查询条件字符串
     * @param page 当前页数
     * @param rows 每页显示的行数
     * @return 返回SearchItem列表和总记录数，封装在SearchResult中
     */
    SearchResult search(String queryString, int page, int rows) throws Exception;
}
