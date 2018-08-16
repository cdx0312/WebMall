package com.cdx.item.pojo;


import com.cdx.domain.TbItem;

/**
 * 商品详情展示页面，封装从索引库查询出来的TbItem对象，增加item_images属性
 * Created by cdx0312
 * 2018/3/9
 */
public class Item extends TbItem {
    public Item(TbItem tbItem) {
        //初始化属性
        this.setId(tbItem.getId());
        this.setTitle(tbItem.getTitle());
        this.setSellPoint(tbItem.getSellPoint());
        this.setPrice(tbItem.getPrice());
        this.setNum(tbItem.getNum());
        this.setBarcode(tbItem.getBarcode());
        this.setImage(tbItem.getImage());
        this.setCid(tbItem.getCid());
        this.setStatus(tbItem.getStatus());
        this.setCreated(tbItem.getCreated());
        this.setUpdated(tbItem.getUpdated());
    }

    /**
     * 添加方法
     * @return
     */
    public String[] getImages() {
        if (this.getImage() != null && !"".equals(this.getImage().trim())) {
            String images2 = this.getImage();
            String[] strings = images2.split(",");
            return strings;
        }
        return null;
    }
}
