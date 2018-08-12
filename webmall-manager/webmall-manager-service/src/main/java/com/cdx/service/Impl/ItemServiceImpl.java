package com.cdx.service.Impl;

import com.cdx.common.domain.EasyUIDataGridResult;
import com.cdx.common.domain.WebMallResponse;
import com.cdx.common.util.IDUtils;
import com.cdx.domain.TbItem;
import com.cdx.domain.TbItemDesc;
import com.cdx.domain.TbItemExample;
import com.cdx.mapper.TbItemDescMapper;
import com.cdx.mapper.TbItemMapper;
import com.cdx.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 商品管理Service
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;

    /**
     * 注入商品描述类的DAO
     */
    @Autowired
    private TbItemDescMapper itemDescMapper;


    @Override
    public TbItem getItemById(Long itemId) {
        return itemMapper.selectByPrimaryKey(itemId);
    }

    /**
     * 实现商品列表的分页展示
     * @param page 当前页数
     * @param rows 每页展示的商品数
     * @return 返回EasyUIDataGridResult给Controller
     */
    @Override
    public EasyUIDataGridResult getItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //执行查询
        TbItemExample example = new TbItemExample();
        List<TbItem> list = itemMapper.selectByExample(example);
        //取查询结果
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        //返回结果
        return result;
    }

    /**
     * 实现添加商品信息的功能
     * @param item 要添加的商品类
     * @param desc 商品描述信息
     * @return 返回定义的WebMallResponse给Controller来展示页面
     */
    @Override
    public WebMallResponse addItem(TbItem item, String desc) {
        //生成商品id
        final long itemId = IDUtils.genItemId();
        //补全item属性
        item.setId(itemId);
        //商品状态:1-正常， 2-下架， 3-删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        //向商品表中插入数据
        itemMapper.insert(item);
        //创建一个商品描述表对应的Pojo
        TbItemDesc itemDesc = new TbItemDesc();
        //补全pojo的属性
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        //向商品描述表插入数据
        itemDescMapper.insert(itemDesc);
        //向ActiveMQ发送商品添加消息
//        jmsTemplate.send(destination, new MessageCreator() {
//            @Override
//            public Message createMessage(Session session) throws JMSException {
//                //发送商品id
//                return session.createTextMessage(itemId + "");
//            }
//        });
        //返回结果
        return WebMallResponse.ok();
    }
}
