package com.ijson.blog.interceptor;

import com.google.common.base.Strings;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.blog.util.DesUtil;
import com.ijson.blog.util.EhcacheUtil;
import com.ijson.blog.util.PassportHelper;
import com.ijson.blog.util.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/1 11:04 AM
 */
@Slf4j
public class RememberLoginCacheInterceptor implements HandlerInterceptor {
    /**
     * 只为设置cookie
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        StopWatch stopWatch = StopWatch.create("RememberLoginCacheInterceptor.preHandle");
        log.info("缓存策略拦截器:{}", request.getRequestURI());

        String remCurrCookie = PassportHelper.getInstance().getRemCurrCookie(request);
        stopWatch.lap("remCurrCookie");
        String cookieValue = DesUtil.decrypt(remCurrCookie);
        stopWatch.lap("des decrypt");
        AuthContext context = (AuthContext) EhcacheUtil.getInstance().get(Constant.remember, cookieValue);
        stopWatch.lap("get remember cookie");
        if (Objects.nonNull(context)) {
            Cookie cookie = new Cookie(PassportHelper.getInstance().getCookieName(), cookieValue);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 30);
            response.addCookie(cookie);
            request.getSession().setAttribute("authContext",context);
        }
        stopWatch.logSlow(10);
        //业务代码
        return true;
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
