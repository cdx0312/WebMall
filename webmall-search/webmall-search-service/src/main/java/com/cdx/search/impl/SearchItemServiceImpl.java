package com.cdx.search.impl;

import com.cdx.common.domain.SearchItem;
import com.cdx.common.domain.WebMallResponse;
import com.cdx.search.mapper.SearchItemMapper;
import com.cdx.search.service.SearchItemService;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品数据导入索引库
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

    /**
     * 注入SearItemMapper接口进行DAO层操作
     */
    @Autowired
    private SearchItemMapper searchItemMapper;
    /**
     * 注入HttpSolrServer到服务中，注入发生在solr.xml文件中
     */
    @Autowired
    private SolrServer solrServer;

    /**
     * 将商品数据从数据库导入到索引库
     * @return ok
     */
    @Override
    public WebMallResponse importItemToIndex() {
        try {
            // 1、查询所有商品数据
            List<SearchItem> itemList = searchItemMapper.getItemList();
            // 2、遍历商品数据添加到索引库
            for (SearchItem searchItem : itemList) {
                // 创建文档对象
                SolrInputDocument document = new SolrInputDocument();
                // 向文档中添加域
                document.addField("id", searchItem.getId());
                document.addField("item_title", searchItem.getTitle());
                document.addField("item_sell_point", searchItem.getSell_point());
                document.addField("item_price", searchItem.getPrice());
                document.addField("item_image", searchItem.getImage());
                document.addField("item_category_name", searchItem.getCategory_name());
                document.addField("item_desc", searchItem.getItem_desc());
                // 把文档写入索引库
                solrServer.add(document);
            }

            // 3、提交
            solrServer.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            return WebMallResponse.build(500, "数据导入失败");
        }
        // 4、添加返回成功
        return WebMallResponse.ok();
    }
}
