package com.cdx.sso.service.Impl;

import com.cdx.common.domain.WebMallResponse;
import com.cdx.common.util.JsonUtils;
import com.cdx.domain.TbUser;
import com.cdx.domain.TbUserExample;
import com.cdx.jedis.JedisClient;
import com.cdx.mapper.TbUserMapper;
import com.cdx.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 用户处理Service
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * 注入查询用户表的DAO
     */
    @Autowired
    private TbUserMapper tbUserMapper;

    /**
     * 注入redis客户端
     */
    @Autowired
    private JedisClient jedisClient;

    /**
     * token前缀
     */
    @Value("${USER_SESSION}")
    private String USER_SESSION;

    @Value("${SESSION_EXPIRE}")
    private Integer SESSION_EXPIRE;


    /**
     * 检查数据是否可用
     * @param data 待检查的数据
     * @param type 数据类型
     * @return WebMallResponse封装的信息
     */
    @Override
    public WebMallResponse checkData(String data, Integer type) {
        //设置查询条件
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        //1--判断用户名是否可用，2--判断手机号是否可用，3--判断邮箱是否可用
        if (type == 1) {
            criteria.andUsernameEqualTo(data);
        } else if (type == 2) {
            criteria.andPhoneEqualTo(data);
        } else if (type == 3) {
            criteria.andEmailEqualTo(data);
        } else {
            return WebMallResponse.build(400, "非法数据");
        }
        //执行查询
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            //查询到数据，数据不可用
            return WebMallResponse.ok(false);
        }
        //数据可用
        return WebMallResponse.ok(true);
    }

    /**
     * 向数据中添加用户
     * @param user 用户的数据信息
     * @return WebMallResponse封装的信息
     */
    @Override
    public WebMallResponse register(TbUser user) {
        //检查数据的有效性
        //判断用户名不能为空
        if (StringUtils.isBlank(user.getUsername())) {
            return WebMallResponse.build(400, "用户名不能为空");
        }
        //判断用户名是否重复
        WebMallResponse webMallResponse = checkData(user.getUsername(), 1);
        if (!(boolean)webMallResponse.getData()) {
            return WebMallResponse.build(400, "用户名重复");
        }
        //判断密码是否为空
        if (StringUtils.isBlank(user.getPassword())) {
            return WebMallResponse.build(400,"密码不能为空");
        }
        //判断电话和email是否重复
        if (StringUtils.isNotBlank(user.getPhone())) {
            webMallResponse = checkData(user.getPhone(), 2);
            if (!(boolean)webMallResponse.getData()) {
                return WebMallResponse.build(400, "电话号码重复");
            }
        }
        if (StringUtils.isNotBlank(user.getEmail())) {
            webMallResponse = checkData(user.getEmail(), 3);
            if (!(boolean)webMallResponse.getData()) {
                return WebMallResponse.build(400, "Email重复");
            }
        }
        //补全POJO的属性，密码要进行md5加密
        user.setCreated(new Date());
        user.setUpdated(new Date());
        String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        //插入数据
        tbUserMapper.insert(user);
        //返回注册成功
        return WebMallResponse.ok();
    }

    /**
     * 登录服务实现类
     * @param username 用户名
     * @param password 密码
     * @return WebMallResponse封装的信息
     */
    @Override
    public WebMallResponse login(String username, String password) {
        //判断用户名和密码是否正确
        TbUserExample example = new TbUserExample();
        TbUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if (list == null || list.size() == 0) {
            //返回登录失败，用户名不存在
            return WebMallResponse.build(400, "用户名不存在");
        }
        TbUser user = list.get(0);
        //密码md5加密再校验
        String md5Pass = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!md5Pass.equals(user.getPassword())) {
            //密码不正确
            return WebMallResponse.build(400, "密码不正确");
        }
        //用UUID生成token
        String token = UUID.randomUUID().toString();
        //把用户信息保存到redis中期 ，key为token，value为用户信息，并设置key的过期时间
        //清空密码
        user.setPassword(null);
        jedisClient.set(USER_SESSION + ":" + token, JsonUtils.objectToJson(user));
        jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
        //返回登录成功，并封装token,token会返回到cookie中去
        return WebMallResponse.ok(token);
    }

    /**
     * 根据token获取用户信息
     * @param token redis中保存的SessionId
     * @return 用户信息封装在WebMallResponse的data中
     */
    @Override
    public WebMallResponse getUserByToken(String token) {
        //从redis中获取json对象
        String json = jedisClient.get(USER_SESSION + ":" + token);
        if (StringUtils.isBlank(json)) {
            return WebMallResponse.build(400, "用户登录已经过期");
        }
        //重置Session的过期时间
        jedisClient.expire(USER_SESSION + ":" + token, SESSION_EXPIRE);
        //把json转换成对象
        TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
        return WebMallResponse.ok(user);
    }

    /**
     * 安全退出登录
     * @param token redis中保存的SessionId
     * @return WebMallResponse封装的信息
     */
    @Override
    public WebMallResponse logout(String token) {
        //从redis中获取json对象
        String json = jedisClient.get(USER_SESSION + ":" + token);
        //JSON为空则返回登录已经过期，否则在redis中删除对应的key
        if (StringUtils.isBlank(json)) {
            return WebMallResponse.build(400, "登录已经过期，无需退出");
        }
        jedisClient.expire(USER_SESSION + ":" + token, 0);
        //返回结果
        return WebMallResponse.ok("");
    }
}
