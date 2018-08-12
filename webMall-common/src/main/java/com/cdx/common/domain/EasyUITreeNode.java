package com.cdx.common.domain;

import java.io.Serializable;

/**
 * 实现商品分类管理功能中，需要传递给页面的信息封装在这个类里面
 */
public class EasyUITreeNode implements Serializable{

    /**
     * 当前节点的Id
     */
    private Long id;

    /**
     * 当前节点的名字
     */
    private String text;

    /**
     * 当前节点的状态，closed或者open
     */
    private String state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
