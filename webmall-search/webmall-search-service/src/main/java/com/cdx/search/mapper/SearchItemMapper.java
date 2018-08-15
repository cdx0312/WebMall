package com.cdx.search.mapper;


import com.cdx.common.domain.SearchItem;

import java.util.List;

/**
 * 查找商品信息到索引表的mapper接口
 */
public interface SearchItemMapper {
    /**
     * 从数据库中获取需要的SearchItem列表信息
     * @return
     */
    List<SearchItem> getItemList();

    /**
     * 根据商品ID，从数据库中查询SearchItem的信息
     * @param itemId
     * @return
     */
    SearchItem getItemById(long itemId);
}
