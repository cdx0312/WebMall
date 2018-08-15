package com.cdx.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * 测试Solr客户端
 */
public class TestSolrJ {

    @Test
    public void testAddDocument() throws Exception {
        //创建一个SolrServer对象
        //指定Solr服务的URL
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr/collection1");
        //创建一个文档对象，SolrInputDocument
        SolrInputDocument document = new SolrInputDocument();
        //向文档中添加域，必须有id域，必须定义在schema.xml中
        document.addField("id", "test001");
        document.addField("item_title", "测试商品1");
        document.addField("item_price", 1000);
        //把文档对象写入索引库
        solrServer.add(document);
        //提交
        solrServer.commit();
    }

    @Test
    public void deleteDocumentById() throws Exception {
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr/collection1");
        solrServer.deleteById("test001");
        solrServer.commit();
    }

    @Test
    public void deleteByQuery() throws Exception {
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr/collection1");
        solrServer.deleteByQuery("id:test001");
        solrServer.commit();
    }

    @Test
    public void searchDocument() throws Exception {
        //创建一个SolrServer对象
        SolrServer solrServer = new HttpSolrServer("http://192.168.25.133:8080/solr/collection1");
        //创建一个SolrQuery对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询条件，分页条件，排序条件，高亮
//        solrQuery.set("q", "*:*");
        solrQuery.setQuery("手机");
        solrQuery.setStart(30);
        solrQuery.setRows(20);
        solrQuery.set("df", "item_keywords");
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("item_title");
        solrQuery.setHighlightSimplePre("<em>");
        solrQuery.setHighlightSimplePost("</em>");
        //执行查询，得到一个Response对象
        QueryResponse response = solrServer.query(solrQuery);
        //取查询结果
        SolrDocumentList solrDocuments = response.getResults();
        //取高亮显示
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for (SolrDocument solrDocument : solrDocuments) {
            System.out.println(solrDocument.getFieldNames());
            System.out.println(solrDocument.get("id"));
            System.out.println(solrDocument.get("item_title"));
            System.out.println("=============================");
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String itemTitle = "";
            if (list != null && list.size() > 0) {
                itemTitle = list.get(0);
            } else {
                itemTitle = (String) solrDocument.get("item_title");
            }
            System.out.println(itemTitle);
        }
        //取查询结果的总记录数
        System.out.println(solrDocuments.getNumFound());
    }

}
