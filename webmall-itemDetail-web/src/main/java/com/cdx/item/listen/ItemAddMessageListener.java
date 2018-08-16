package com.cdx.item.listen;

import com.cdx.domain.TbItem;
import com.cdx.domain.TbItemDesc;
import com.cdx.item.pojo.Item;
import com.cdx.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * 商品添加信息监听器
 * Created by cdx0312
 * 2018/3/11
 */
public class ItemAddMessageListener implements MessageListener{

    /**
     * 注入商品服务
     */
    @Autowired
    private ItemService itemService;

    /**
     * 注入FreeMarkerConfigeur
     * @param message
     */
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 注入HTML静态页面输出路径
     */
    @Value("${HTML_OUT_PATH}")
    private String HTML_OUT_PATH;

    /**
     * 实现监听方法
     * @param message
     */
    @Override
    public void onMessage(Message message) {
        try {
            //从消息中取商品ID
            TextMessage textMessage = (TextMessage) message;
            String strId = textMessage.getText();
            Long itemId = Long.parseLong(strId);
            //等待事务提交
            Thread.sleep(1000);
            //根据商品Id查询商品信息及商品描述
            TbItem tbItem = itemService.getItemById(itemId);
            Item item = new Item(tbItem);
            TbItemDesc itemDesc = itemService.getItemDescById(itemId);
            //使用FreeMarker生成静态页面
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            //创建模板
            //加载模板
            Template template = configuration.getTemplate("item.ftl");
            //准备模板需要的数据
            Map data = new HashMap();
            data.put("item", item);
            data.put("itemDesc", itemDesc);
            //指定输出的目录及文件名
            Writer out = new FileWriter(new File(HTML_OUT_PATH + strId + ".html"));

            template.process(data, out);

            out.close();

        } catch (JMSException | IOException | TemplateException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
