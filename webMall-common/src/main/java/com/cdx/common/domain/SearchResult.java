package com.cdx.common.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cdx0312
 * 2018/3/8
 * 包装搜索结果的类
 */
public class SearchResult implements Serializable{
    /**
     * 总页数
     */
    private long totalPages;

    /**
     * 总记录数
     */
    private long recordCount;

    /**
     * 搜索结果列表
     */
    private List<SearchItem> itemList;

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public List<SearchItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<SearchItem> itemList) {
        this.itemList = itemList;
    }
}
