package com.cdx.sso.controller;

import com.cdx.common.domain.WebMallResponse;
import com.cdx.common.util.CookieUtils;
import com.cdx.domain.TbUser;
import com.cdx.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户处理Controller
 * Created by cdx0312
 */
@Controller
public class UserController {

    /**
     * 注入userService
     */
    @Autowired
    private UserService userService;

    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    /**
     * 校验数据有效性的Controller
     * @param param 要校验的参数
     * @param type 参数类型
     * @return WebMallResponse，包含status,msg,data
     */
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public WebMallResponse checkUserData(@PathVariable String param, @PathVariable Integer type) {
        return userService.checkData(param, type);
    }

    /**
     * 用户注册Controller
     * @param user 要注册的用户
     * @return WebMallResponse，包含status,msg,data
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public WebMallResponse register(TbUser user) {
        return userService.register(user);
    }

    /**
     * 用户登录Controller
     * @param username 用户名
     * @param password 密码
     * @return WebMallResponse，包含status,msg,data
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public WebMallResponse login(String username, String password,
                              HttpServletResponse response,
                              HttpServletRequest request) {
        WebMallResponse result = userService.login(username, password);
        //写入Cookie,response
        if (result.getStatus() == 200) {
            CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
        }
        return result;
    }

    /**
     * 根据Token获取用户信息
     * @param token redis中保存的sessionId，从路径中获取
     * @return 封装了用户信息的WebMallResponse
     */
//    @RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET,
//            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public String getUserByToken(@PathVariable String token, String callback) {
//        WebMallResponse result = userService.getUserByToken(token);
//        //是否为JsonP请求
//        if (StringUtils.isNotBlank(callback)) {
//            return callback + "(" + JsonUtils.objectToJson(result) + ");";
//        }
//        return JsonUtils.objectToJson(result);
//    }
    //第二种Jsonp方法，Spring4.1以上版本
    @RequestMapping(value = "/user/token/{token}", method = RequestMethod.GET)
    @ResponseBody
    public Object getUserByToken(@PathVariable String token, String callback) {
        WebMallResponse result = userService.getUserByToken(token);
        //是否为JsonP请求
        if (StringUtils.isNotBlank(callback)) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            //设置回调方法
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }

    /**
     * 安全退出
     * @param token SessionId值
     * @return WebMallResponse，包含status,msg,data
     */
    @RequestMapping(value = "/user/logout/{token}", method = RequestMethod.GET)
    @ResponseBody
    public WebMallResponse logout(@PathVariable String token) {
        return userService.logout(token);
    }


}
