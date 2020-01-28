package com.ijson.blog.interceptor;

import com.google.common.base.Strings;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.blog.util.DesUtil;
import com.ijson.blog.util.EhcacheUtil;
import com.ijson.blog.util.PassportHelper;
import com.ijson.blog.util.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/18 12:42 PM
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private static PathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 预处理回调方法，实现处理器的预处理
     * 返回值：true表示继续流程；false表示流程中断，不会继续调用其他的拦截器或处理器
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        StopWatch stopWatch = StopWatch.create("LoginInterceptor.preHandle");
        String uri = request.getRequestURI();
        log.info("登录拦截器:{}", uri);

        String cookieValue = PassportHelper.getInstance().getCurrCookie(request);
        stopWatch.lap("cookieValue");
        String remCurrCookie = PassportHelper.getInstance().getRemCurrCookie(request);
        if (Strings.isNullOrEmpty(cookieValue) && Strings.isNullOrEmpty(remCurrCookie)) {
            response.sendRedirect("/");
            return false;
        }
        stopWatch.lap("remCurrCookie");
        String rememberCookieValue = null;
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.loginUserCacheKey, cookieValue);
        stopWatch.lap("getLoginUserCacheKey");
        if (Objects.isNull(context)) {
            if (Strings.isNullOrEmpty(remCurrCookie)) {
                PassportHelper.getInstance().removeCookie(request, response);
                request.getSession().removeAttribute(cookieValue);
                response.sendRedirect("/");
                return false;
            }
            rememberCookieValue = DesUtil.decrypt(remCurrCookie);
            context = (AuthContext) EhcacheUtil.getInstance().get(Constant.remember, rememberCookieValue);
            if (Objects.isNull(context)) {
                PassportHelper.getInstance().removeCookie(request, response);
                request.getSession().removeAttribute(cookieValue);
                response.sendRedirect("/");
                return false;
            }
        }
        List<String> permissionPath = context.getPermissionPath();
        if (!isParadigm(permissionPath, uri)) {
            response.sendRedirect("/");
            return false;
        }

        if (!Strings.isNullOrEmpty(rememberCookieValue)) {
            Cookie cookie = new Cookie(PassportHelper.getInstance().getCookieName(), rememberCookieValue);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 30);
            response.addCookie(cookie);
        }
        stopWatch.lap("checkContext");
        request.getSession().setAttribute("authContext", context);
        //业务代码
        stopWatch.logSlow(10);
        return true;
    }


    public static boolean isParadigm(List<String> permissionPath, String uri) {
        boolean flag = false;
        for (String path : permissionPath) {
            if (pathMatcher.match(path, uri)) {
                flag = true;
                break;
            }
        }
        return flag;

    }

    public static void main(String[] args) {
        System.out.println(pathMatcher.extractPathWithinPattern("/post/enable/*/*", "/post/enable/cuiyongxu/338828383"));
        System.out.println(pathMatcher.extractUriTemplateVariables("/post/enable/{a}/{b}", "/post/enable/cuiyongxu/338828383"));
        System.out.println(pathMatcher.match("/post/enable/*/*", "/post/enab3le/cuiyongxu/338828383"));
        System.out.println(pathMatcher.match("/post/enable/*/*", "/post/enable/cuiyongxu/338828383"));
        System.out.println(pathMatcher.match("/post/create", "/post/create"));
    }

    /**
     * 后处理回调方法，实现处理器（controller）的后处理，但在渲染视图之前
     * 此时我们可以通过modelAndView对模型数据进行处理或对视图进行处理
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    /**
     * 整个请求处理完毕回调方法，即在视图渲染完毕时回调，
     * 如性能监控中我们可以在此记录结束时间并输出消耗时间，
     * 还可以进行一些资源清理，类似于try-catch-finally中的finally，
     * 但仅调用处理器执行链中
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
