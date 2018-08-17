package com.cdx.order.interceptor;

import com.cdx.common.domain.WebMallResponse;
import com.cdx.common.util.CookieUtils;
import com.cdx.domain.TbUser;
import com.cdx.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 判断用户是否登录的拦截器
 * Created by cdx0312
 */
public class LoginInterceptor implements HandlerInterceptor{

    @Autowired
    private UserService userService;

    @Value("${TOKEN_KEY}")
    private String TOKEN_KEY;

    @Value("${SSO_URL}")
    private String SSO_URL;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //执行Handler之前执行此方法，
        //1、从Cookie中取Token信息
        String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
        //2、取不到token，跳转到sso的登录页面，需要把当前请求的url作为参数传递给sso，sso登录成功之后跳转回当前请求的页面
        if (StringUtils.isBlank(token)) {
            String requestURL = request.getRequestURL().toString();
            response.sendRedirect(SSO_URL + "/page/login?url=" + requestURL);
            return false;
        }
        //3、取到token，调用sso系统的服务来判断用户是否登录
        //4、用户未登录，即没有取到用户信息，sso登录页面
        WebMallResponse result = userService.getUserByToken(token);
        if (result.getStatus() != 200) {
            String requestURL = request.getRequestURL().toString();
            response.sendRedirect(SSO_URL + "/page/login?url=" + requestURL);
            return false;
        }
        //5、如果取到用户信息，则已经登录，放行,把用户信息放入request中
        TbUser user = (TbUser) result.getData();
        request.setAttribute("user", user);
        // 返回true放行，返回false拦截
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //handler执行之后，modelAndView返回之前

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //modelAndView返回之后执行
    }
}
