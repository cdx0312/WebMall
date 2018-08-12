package com.cdx.service;

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
}
