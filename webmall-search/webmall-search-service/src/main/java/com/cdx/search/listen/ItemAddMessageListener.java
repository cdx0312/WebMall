package com.cdx.search.listen;

import com.cdx.common.domain.SearchItem;
import com.cdx.search.mapper.SearchItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;

/**
 * 监听商品添加时间，同步索引库
 * Created by cdx0312
 * 2018/3/9
 */
public class ItemAddMessageListener implements MessageListener{

    /**
     * 注入DAO
     */
    @Autowired
    private SearchItemMapper searchItemMapper;
    /**
     * 注入SolrServer对象
     */
    @Autowired
    private SolrServer solrServer;

    @Override
    public void onMessage(Message message) {
        try {
            //从消息中取商品id
            TextMessage textMessage = (TextMessage) message;
            String text = textMessage.getText();
            long itemId = Long.parseLong(text);
            //根据商品id查询数据库，取商品信息,需要设置等待时间，等待事务提交
            Thread.sleep(1000);
            SearchItem searchItem = searchItemMapper.getItemById(itemId);
            //创建文档对象
            SolrInputDocument document = new SolrInputDocument();
            //向文档对象中添加域
            document.addField("id", searchItem.getId());
            document.addField("item_title", searchItem.getTitle());
            document.addField("item_sell_point", searchItem.getSell_point());
            document.addField("item_price", searchItem.getPrice());
            document.addField("item_image", searchItem.getImage());
            document.addField("item_category_name", searchItem.getCategory_name());
            document.addField("item_desc", searchItem.getItem_desc());
            //把文档对象写入索引库
            solrServer.add(document);
            //提交
            solrServer.commit();
        } catch (JMSException | InterruptedException | SolrServerException | IOException e) {
            e.printStackTrace();
        }

    }
}
