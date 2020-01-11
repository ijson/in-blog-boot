package com.ijson.blog.interceptor;

import com.ijson.blog.manager.ViewSyncManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/24 2:55 PM
 */
@Slf4j
public class AccessStatisticInterceptor implements WebRequestInterceptor {

    private ViewSyncManager viewSyncManager;

    public AccessStatisticInterceptor(ViewSyncManager viewSyncManager) {
        this.viewSyncManager = viewSyncManager;
    }


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public void preHandle(WebRequest webRequest) throws Exception {
        DispatcherServletWebRequest request = (DispatcherServletWebRequest) webRequest;
        HttpServletRequest httpSelectRequest = request.getRequest();


        String nowTime = simpleDateFormat.format(new Date());
        String ip = httpSelectRequest.getRemoteAddr();
        String method = request.getHttpMethod().name();
        String language = request.getLocale().getLanguage();
        String requestURI = httpSelectRequest.getRequestURI();


        log.info("访问时间:{},访问IP:{},访问方式:{},访问语言:{},访问路径:{}", nowTime
                , ip, method, language, requestURI);
        viewSyncManager.syncWebSite();
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {

    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {

    }
}
