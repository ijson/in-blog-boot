package com.ijson.blog.configuration;

import com.ijson.blog.interceptor.AccessStatisticInterceptor;
import com.ijson.blog.interceptor.LoginInterceptor;
import com.ijson.blog.interceptor.RememberLoginCacheInterceptor;
import com.ijson.blog.manager.ViewSyncManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/21 2:12 PM
 */
@Configuration
public class DefaultView implements WebMvcConfigurer {

    @Autowired
    private ViewSyncManager viewSyncManager;

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("view/index.html");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RememberLoginCacheInterceptor()).addPathPatterns(
                "/**"
        );

        // 可添加多个
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns(
                "/admin/**",
                "/post/**",
                "/auth/**",
                "/blogroll/**",
                "/draft/**",
                "/role/**",
                "/user/**",
                "/site/**"
        ).excludePathPatterns(
                "/user/edit/image");


        registry.addWebRequestInterceptor(new AccessStatisticInterceptor(viewSyncManager)).addPathPatterns(
                "/index",
                "/detail/**",
                "/tags",
                "/tags/**",
                "/"
        );
    }
}
