package com.cdx.search.service;


import com.cdx.common.domain.WebMallResponse;

/**
 * 搜索商品服务
 */
public interface SearchItemService {
    /**
     * 将商品导入索引库
     * @return 返回ok
     */
    WebMallResponse importItemToIndex();
}
