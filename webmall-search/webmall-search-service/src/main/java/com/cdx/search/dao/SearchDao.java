package com.cdx.search.dao;

import com.cdx.common.domain.SearchItem;
import com.cdx.common.domain.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 查询索引库商品的DAO
 * Created by cdx0312
 * 2018/3/8
 */

@Repository
public class SearchDao {

    /**
     * 注入SolrServer对象
     */
    @Autowired
    private SolrServer solrServer;

    /**
     * 查询索引库商品
     * @param query 查询语句
     * @return 封装了商品列表，总记录数和总页数
     * @throws Exception
     */
    public SearchResult searh(SolrQuery query) throws Exception{
        //根据Query对象进行查询
        QueryResponse response = solrServer.query(query);
        //取查询结果
        SolrDocumentList solrDocuments = response.getResults();
        //取查询结果的总记录数并封装起来
        long numFound = solrDocuments.getNumFound();
        SearchResult result = new SearchResult();
        result.setRecordCount(numFound);
        List<SearchItem> itemList = new ArrayList<>();
        for (SolrDocument solrDocument : solrDocuments) {
            SearchItem item = new SearchItem();
            item.setCategory_name((String) solrDocument.get("item_category_name"));
            item.setId((String) solrDocument.get("id"));
            //取一张图片
            String image = (String) solrDocument.get("item_image");
            if (StringUtils.isNotBlank(image)){
                image = image.split(",")[0];
            }
            item.setImage(image);
            item.setPrice((Long) solrDocument.get("item_price"));
            item.setSell_point((String) solrDocument.get("item_sell_point"));
            //取高亮显示
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String title;
            if (list != null && list.size()>0) {
                title = list.get(0);
            } else {
                title = (String) solrDocument.get("item_title");
            }
            item.setTitle(title);
            //将商品添加到商品列表中去
            itemList.add(item);
        }
        result.setItemList(itemList);
        //返回结果
        return result;
    }
}
