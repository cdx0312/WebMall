package com.cdx.sso.service;

import com.cdx.common.domain.WebMallResponse;
import com.cdx.domain.TbUser;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 检查数据是否可用
     * @param data 待检查的数据
     * @param type 数据类型
     * @return WebMallResponse封装的信息
     */
    WebMallResponse checkData(String data, Integer type);

    /**
     * 向数据中添加用户
     * @param user 用户的数据信息
     * @return WebMallResponse封装的信息
     */
    WebMallResponse register(TbUser user);

    /**
     * 登录服务
     * @param username 用户名
     * @param password 密码
     * @return WebMallResponse封装的信息
     */
    WebMallResponse login(String username, String password);

    /**
     * 根据token获取用户信息
     * @param token redis中保存的SessionId
     * @return 用户信息封装在WebMallResponse的data中
     */
    WebMallResponse getUserByToken(String token);

    /**
     * 安全退出登录
     * @param token redis中保存的SessionId
     * @return WebMallResponse封装的信息
     */
    WebMallResponse logout(String token);
}
