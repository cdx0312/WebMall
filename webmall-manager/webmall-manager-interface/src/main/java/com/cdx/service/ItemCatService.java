package com.cdx.service;

import com.cdx.common.domain.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
    List<EasyUITreeNode> getItemCat(long parentId);
}
