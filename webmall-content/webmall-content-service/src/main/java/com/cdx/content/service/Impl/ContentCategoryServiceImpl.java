package com.cdx.content.service.Impl;

import com.cdx.common.domain.EasyUITreeNode;
import com.cdx.common.domain.WebMallResponse;
import com.cdx.content.service.ContentCategoryService;
import com.cdx.domain.TbContentCategory;
import com.cdx.domain.TbContentCategoryExample;
import com.cdx.mapper.TbContentCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 内容分类管理Service
 */

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    /**
     * 注入内容分类管理DAO
     */
    @Autowired
    private TbContentCategoryMapper contentCategoryMapper;

    /**
     * 获取内容分类的树的实现，当前节点的Id作为其父节点，展开显示其子节点
     * @param parentId 当前节点的id作为其子节点的父节点
     * @return 返回封装好的EasyUITreeNode类
     */
    @Override
    public List<EasyUITreeNode> getContentCategoryList(long parentId) {
        //根据parentID查询子节点列表
        TbContentCategoryExample example = new TbContentCategoryExample();
        //设置查询条件
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        //查询结果
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        //将list的值转换成需要返回的EasyUITreeNode列表
        List<EasyUITreeNode> result = new ArrayList<>();
        for (TbContentCategory tbContentCategory : list) {
            EasyUITreeNode node = new EasyUITreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            //添加到结果列表
            result.add(node);
        }

        return result;
    }

    /**
     * 添加内容类别，将当前节点的ID作为新接待点的parentId
     * @param parentId 新增节点的父ID
     * @param name 新增节点的名字
     * @return 返回TaotaoResult封装的类
     */
    @Override
    public WebMallResponse addContentCategory(Long parentId, String name) {
        //创建一个pojo对象
        TbContentCategory contentCategory = new TbContentCategory();
        //补全对象的属性
        contentCategory.setParentId(parentId);
        contentCategory.setName(name);
        //状态：1-正常 2-删除
        contentCategory.setStatus(1);
        contentCategory.setSortOrder(1);
        contentCategory.setIsParent(false);
        contentCategory.setCreated(new Date());
        contentCategory.setUpdated(new Date());
        //插入到数据库
        contentCategoryMapper.insert(contentCategory);
        //判断父节点的状态，若其之前为叶子节点，则将其改为父节点
        TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
        if (!parent.getIsParent()) {
            parent.setIsParent(true);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        //返回结果
        return WebMallResponse.ok(contentCategory);
    }

    /**
     * 修改内容类别名称
     * @param id 要修改的节点的Id
     * @param name 修改后节点的名字
     * @return 返回TaotaoResult封装好的实体类
     */
    @Override
    public WebMallResponse updateContentCategory(Long id, String name) {
        //根据节点Id，获取节点的对象
        TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
        //修改节点的名字
        contentCategory.setName(name);
        //保存到数据库
        contentCategoryMapper.updateByPrimaryKey(contentCategory);
        //返回结果
        return WebMallResponse.ok(contentCategory);
    }

    /**
     * 删除当前节点：
     *         1、当前节点为叶子节点，直接删除就可以
     *              1.1、当前接待你为其父节点的唯一子节点，修改其父节点为叶子节点
     *              1.2、其父节点还存在其他子节点，不修改父节点
     *         2、当前节点为父节点，递归删除所有子节点
     * @param id 当前节点的Id
     */
    @Override
    public void deleteContentCategory(Long id) {
        //获取当前节点的对象
        TbContentCategory currentNode = contentCategoryMapper.selectByPrimaryKey(id);
        //查看其是否为叶子节点
        if (currentNode.getIsParent()) {
            //其存在子节点，需要递归删除
            //获取当前节点为父节点的所有子节点列表
            TbContentCategoryExample example = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria criteria = example.createCriteria();
            criteria.andParentIdEqualTo(id);
            List<TbContentCategory> sublist = contentCategoryMapper.selectByExample(example);
            for (TbContentCategory tbContentCategory : sublist) {
                deleteContentCategory(tbContentCategory.getId());
            }
        }
        //查看当前节点是否为其父节点的唯一子节点
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(currentNode.getParentId());
        List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
        //如果其为唯一子节点，修改其父节点的isParent属性为false，然后删除该子节点，
        // 如果其父节点存在其他子节点，则直接删除该节点
        if (list.size() == 1) {
            //该节点为其父节点的唯一子节点
            //获取该节点的父节点
            TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(currentNode.getParentId());
            parent.setIsParent(false);
            contentCategoryMapper.updateByPrimaryKey(parent);
        }
        //删除该节点
        contentCategoryMapper.deleteByPrimaryKey(id);
        //递归结束
        return;
    }
}
