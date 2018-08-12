package com.cdx.service;

import com.cdx.common.domain.EasyUIDataGridResult;
import com.cdx.common.domain.WebMallResponse;
import com.cdx.domain.TbItem;

/**
 * 商品管理服务的接口
 */
public interface ItemService {
    /**
     * 根据商品Id来查询商品信息
     * @param itemId 商品ID
     * @return 返回商品的实体类
     */
    TbItem getItemById(Long itemId);

    /**
     * 实现商品列表的分页展示
     * @param page 当前页数
     * @param rows 每页展示的商品数
     * @return 返回EasyUIDataGridResult给Controller
     */
    EasyUIDataGridResult getItemList(int page, int rows);

    WebMallResponse addItem(TbItem item, String desc);
}
