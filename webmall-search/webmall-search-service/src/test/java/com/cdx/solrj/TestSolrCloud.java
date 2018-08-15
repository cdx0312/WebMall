package com.cdx.solrj;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by cdx0312
 * 2018/3/8
 */
public class TestSolrCloud {
    @Test
    public void testSolrCloudAddDocument() throws IOException, SolrServerException {
        //创建一个CloudSolrServer对象，构造方法中需要制定zookeeper的地址列表
        CloudSolrServer cloudSolrServer = new CloudSolrServer("192.168.25.133:2181,192.168.25.133:2182,192.168.25.133:2183");
        //需要设定的默认Collection
        cloudSolrServer.setDefaultCollection("collection2");
        //创建一个文档对象
        SolrInputDocument document = new SolrInputDocument();
        //向文档中添加域
        document.addField("id", "test001");
        document.addField("item_title", "测试商品名称");
        document.addField("item_price", 100);
        //把文档写入索引库
        cloudSolrServer.add(document);
        //提交
        cloudSolrServer.commit();
    }
}
