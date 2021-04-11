package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.ConfigEntity;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.info.IndexInstall;
import com.ijson.blog.service.model.info.TencentInfo;
import com.ijson.blog.service.model.info.ThemeInfo;
import com.ijson.blog.service.model.info.WebSiteInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/22 11:05 PM
 */
@Slf4j
@RestController
@RequestMapping("/site")
public class WebSiteAction extends BaseController {


    @PostMapping("/website")
    public Result siteForm(HttpServletRequest request, HttpSession session, @RequestBody WebSiteInfo post) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        if (Strings.isNullOrEmpty(post.getSiteName())) {
            return Result.error(BlogBusinessExceptionCode.WEBSITE_NAME_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(post.getRegRoleId())) {
            return Result.error(BlogBusinessExceptionCode.REG_ROLE_CANNOT_BE_EMPTY);
        }

        post.setType("all");
        ConfigEntity entity = webSiteService.updateWebSite(post);
        return Result.ok("保存网站信息成功!");
    }


    @PostMapping("/tencent")
    public Result tencent(HttpServletRequest request, HttpSession session, @RequestBody TencentInfo tencentInfo) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        if (Strings.isNullOrEmpty(tencentInfo.getAppId())) {
            return Result.error(BlogBusinessExceptionCode.APPID_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(tencentInfo.getAppKey())) {
            return Result.error(BlogBusinessExceptionCode.APPKEY_CANNOT_BE_EMPTY);
        }
        ConfigEntity entity = webSiteService.updateTencent(tencentInfo);
        return Result.ok("保存腾讯信息成功!");
    }


    @PostMapping("/theme")
    public Result theme(HttpServletRequest request, HttpSession session, @RequestBody ThemeInfo themeInfo) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);

        if (Strings.isNullOrEmpty(themeInfo.getViewTheme())) {
            return Result.error(BlogBusinessExceptionCode.FRONTEND_THEME_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(themeInfo.getAdminTheme())) {
            return Result.error(BlogBusinessExceptionCode.BACKEND_THEME_CANNOT_BE_EMPTY);
        }


        ConfigEntity entity = webSiteService.updateDefaultTheme(themeInfo);
        return Result.ok("保存主题信息成功!");
    }


    @PostMapping("/switch/{type}")
    public Result webSwitch(HttpServletRequest request, HttpSession session, @PathVariable("type") String type) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        ConfigEntity entity = webSiteService.updateSwitch(type);
        return Result.ok("更新成功!");
    }


    @PostMapping("/field/show/{name}")
    public Result showField(HttpServletRequest request, HttpSession session, @PathVariable("name") String name) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        webSiteService.updateShowField(name);
        return Result.ok("更新成功!");
    }

}
