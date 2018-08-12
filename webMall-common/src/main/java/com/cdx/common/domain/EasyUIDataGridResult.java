package com.cdx.common.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 保存商品查询过程中，分页展示需要的数据
 * total表示总商品数，rows表示商品信息的集合
 */
public class EasyUIDataGridResult implements Serializable{

    //商品总数
    private Long total;
    //商品信息列表
    private List rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
