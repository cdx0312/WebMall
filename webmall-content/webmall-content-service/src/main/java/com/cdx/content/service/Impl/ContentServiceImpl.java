package com.cdx.content.service.Impl;

import com.cdx.common.domain.EasyUIDataGridResult;
import com.cdx.common.domain.WebMallResponse;
import com.cdx.common.util.JsonUtils;
import com.cdx.content.service.ContentService;
import com.cdx.domain.TbContent;
import com.cdx.domain.TbContentExample;
import com.cdx.jedis.JedisClient;
import com.cdx.mapper.TbContentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 内容管理服务的实现类
 */
@Service
public class ContentServiceImpl implements ContentService {

    /**
     * 注入内容管理DAO
     */
    @Autowired
    private TbContentMapper contentMapper;

    /**
     * 注入JedisClient对象来操作redis缓存
     */
    @Autowired
    private JedisClient jedisClient;

    /**
     * 注入properties中的属性
     */
    @Value("${INDEX_CONTENT}")
    private String INDEX_CONTENT;

    /**
     * 分页展示符合条件的内容
     * @param page 当前页
     * @param rows 每页展示的项目数量
     * @param categoryId 种类列表
     * @return 返回EasyUIDataGridResult给Controller
     */
    @Override
    public EasyUIDataGridResult getContentList(int page, int rows, long categoryId) {
        //设置分页信息
        PageHelper.startPage(page,rows);
        //执行查询
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> list = contentMapper.selectByExample(example);
        //取查询结果
        PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(list);
        EasyUIDataGridResult result = new EasyUIDataGridResult();
        result.setRows(list);
        result.setTotal(pageInfo.getTotal());
        //返回结果
        return result;
    }

    /**
     * 内容添加功能
     * @param content 要添加的内容的实现类
     * @return 返回result封装的数据
     */
    @Override
    public WebMallResponse addContent(TbContent content) {
        //补全pojo的属性
        content.setCreated(new Date());
        content.setUpdated(new Date());
        //插入到内容表
        contentMapper.insert(content);
        //同步缓存，删除对应的缓存信息
        jedisClient.hdel(INDEX_CONTENT, content.getCategoryId().toString());
        return WebMallResponse.ok();
    }

    /**
     * 内容修改功能
     * @param content 要修改的内容的实现类
     * @return 返回Result封装的数据
     */
    @Override
    public WebMallResponse editContent(TbContent content) {
        //更新原有content的值
        content.setUpdated(new Date());
        //更新到内容表
        contentMapper.updateByPrimaryKey(content);
        //返回结果
        return WebMallResponse.ok();
    }

    /**
     * 内容删除服务功能
     * @param ids 页面接收的字符串，各个id之间用逗号分隔
     * @return 返回Result封装的数据
     */
    @Override
    public WebMallResponse deleteContentByIds(String ids) {
        //依次删除
        String[] list = ids.split(",");
        for (int i = 0; i < list.length; i++) {
            contentMapper.deleteByPrimaryKey(Long.valueOf(list[i]));
        }
        //返回结果
        return WebMallResponse.ok();
    }

    /**
     * 通过分类id获取内容列表
     * @param cid 分类id
     * @return 内容信息列表
     */
    @Override
    public List<TbContent> getContentByCid(Long cid) {
        //先查询缓存
        //添加缓存不能影响正常业务逻辑
        try {
            //查询缓存，如果查到了将JSON转换成list返回
            String json = jedisClient.hget(INDEX_CONTENT, cid + "");
            if (StringUtils.isNotBlank(json)) {
                List<TbContent> redisList = JsonUtils.jsonToList(json, TbContent.class);
                return redisList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //缓存中没有命中则需要查询数据库
        //设置查询条件
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(cid);
        //执行查询
        List<TbContent> list = contentMapper.selectByExample(example);
        //将查询结果添加到缓存
        try {
            jedisClient.set("1", "1");
            jedisClient.hset(INDEX_CONTENT, cid + "" , JsonUtils.objectToJson(list));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return list;
    }

}
