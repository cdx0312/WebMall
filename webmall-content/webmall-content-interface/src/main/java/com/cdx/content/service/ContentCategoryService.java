package com.cdx.content.service;

import com.cdx.common.domain.EasyUITreeNode;
import com.cdx.common.domain.WebMallResponse;

import java.util.List;

/**
 * 内容分类管理服务接口
 */
public interface ContentCategoryService {
    /**
     * 获取内容分类的树的实现，当前节点的Id作为其父节点，展开显示其子节点
     * @param parentId 当前节点的id作为其子节点的父节点
     * @return 返回封装好的EasyUITreeNode类
     */
    List<EasyUITreeNode> getContentCategoryList(long parentId);

    /**
     * 添加内容类别，将当前节点的ID作为新接待点的parentId
     * @param parentId 新增节点的父ID
     * @param name 新增节点的名字
     * @return 返回封装的类
     */
    WebMallResponse addContentCategory(Long parentId, String name);

    /**
     * 修改内容类别名称
     * @param id 要修改的节点的Id
     * @param name 修改后节点的名字
     * @return 返回封装好的实体类
     */
    WebMallResponse updateContentCategory(Long id, String name);

    /**
     * 删除当前节点
     * @param id 当前节点的Id
     */
    void deleteContentCategory(Long id);
}
