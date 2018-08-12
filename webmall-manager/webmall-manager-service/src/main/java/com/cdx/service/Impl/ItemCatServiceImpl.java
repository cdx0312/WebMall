package com.cdx.service.Impl;

import com.cdx.common.domain.EasyUITreeNode;
import com.cdx.domain.TbItemCat;
import com.cdx.domain.TbItemCatExample;
import com.cdx.mapper.TbItemCatMapper;
import com.cdx.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类管理
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    /**
     * 注入itemCatMapper，来完成对数据库的查找
     */
    @Autowired
    private TbItemCatMapper itemCatMapper;

    /**
     * 根据父节点来查询该节点的子节点，并将查询到的商品分类
     * 列表转换成EasyUITreeNode列表，将其传递给商品分类Controller
     * @param parentId
     * @return
     */
    @Override
    public List<EasyUITreeNode> getItemCat(long parentId) {
        //根据父节点的id来查询子节点列表
        TbItemCatExample example = new TbItemCatExample();
        //设置查询条件
        TbItemCatExample.Criteria criteria = example.createCriteria();
        //设置parentId
        criteria.andParentIdEqualTo(parentId);
        //执行查询
        List<TbItemCat> list = itemCatMapper.selectByExample(example);
        //转换成EasyUITreeNode列表
        List<EasyUITreeNode> resultList = new ArrayList<>();
        for (TbItemCat tbItemCat : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbItemCat.getId());
            node.setText(tbItemCat.getName());
            //如果节点下有子节点就是closed，如果没有子节点为open
            node.setState(tbItemCat.getIsParent()?"closed":"open");
            //添加到节点列表
            resultList.add(node);
        }
        return resultList;
    }
}
