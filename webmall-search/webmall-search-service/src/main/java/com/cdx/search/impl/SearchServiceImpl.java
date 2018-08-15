package com.cdx.search.impl;

import com.cdx.common.domain.SearchResult;
import com.cdx.search.dao.SearchDao;
import com.cdx.search.service.SearchService;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 搜索服务功能实现
 * Created by cdx0312
 * 2018/3/8
 */
@Service
public class SearchServiceImpl implements SearchService {

    /**
     * 注入SearchDao对象
     */
    @Autowired
    private SearchDao searchDao;

    /**
     * 查询索引库，返回结果
     * @param queryString 查询条件字符串
     * @param page 当前页数
     * @param rows 每页显示的行数
     * @return 返回SearchItem列表和总记录数，封装在SearchResult中
     */
    @Override
    public SearchResult search(String queryString, int page, int rows) throws Exception {
        //根据查询条件拼装查询对象
        //创建一个SolrQuert对象
        SolrQuery query = new SolrQuery();
        //设置查询条件
        query.setQuery(queryString);
        //设置分页条件
        if (page < 1)
            page = 1;
        query.setStart((page-1)*rows);
        query.setRows(rows);
        //设置默认搜索域
        query.set("df", "item_title");
        //设置高亮显示
        query.setHighlight(true);
        query.addHighlightField("item_title");
        query.setHighlightSimplePre("<font color='red'>");
        query.setHighlightSimplePost("</font>");
        //调用dao执行查询
        SearchResult searchResult = searchDao.searh(query);
        //计算总页数
        long recordCount = searchResult.getRecordCount();
        long pages = recordCount / rows;
        if (recordCount % rows > 0)
            pages++;
        searchResult.setTotalPages(pages);
        //返回结果
        return searchResult;
    }
}
