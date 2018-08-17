package com.cdx.content.service;

import com.cdx.common.domain.EasyUIDataGridResult;
import com.cdx.common.domain.WebMallResponse;
import com.cdx.domain.TbContent;

import java.util.List;

/**
 * 商品内容管理Service的接口
 */
public interface ContentService {
    /**
     * 展示内容列表
     * @param page 当前页
     * @param rows 每页展示的项目数量
     * @param categoryId 种类列表
     * @return 封装了页面展示信息的EasyUiDataGrieResult类
     */
    EasyUIDataGridResult getContentList(int page, int rows, long categoryId);

    /**
     * 内容添加功能
     * @param content 要添加的内容的实现类
     * @return 返回封装的数据
     */
    WebMallResponse addContent(TbContent content);

    /**
     * 内容修改功能
     * @param content 要修改的内容的实现类
     * @return 返回封装的数据
     */
    WebMallResponse editContent(TbContent content);

    /**
     * 根据内容id,批量删除内容
     * @param ids 页面接收的字符串，各个id之间用", "分隔
     * @return 返回封装的数据
     */
    WebMallResponse deleteContentByIds(String ids);

    /**
     * 根据分类id,获取相关的内容列表
     * @param cid 分类id
     * @return 返回分类id对应的内容列表
     */
    List<TbContent> getContentByCid(Long cid);

}
