package com.ijson.blog.controller.admin;

import com.ijson.blog.controller.BaseController;
import com.ijson.blog.controller.admin.model.UpdPassword;
import com.ijson.blog.dao.entity.ConfigEntity;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.WebSite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

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
    public Result siteForm(HttpServletRequest request, HttpSession session, @RequestBody WebSite post) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            return Result.error(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        ConfigEntity entity = webSiteService.updateWebSite(post);
        return Result.ok("保存网站信息成功!");
    }


    @PostMapping("/switch/{type}")
    public Result webSwitch(HttpServletRequest request, HttpSession session, @PathVariable("type") String type) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            return Result.error(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        ConfigEntity entity = webSiteService.updateSwitch(type);
        return Result.ok("更新成功!");
    }


    @PostMapping("/field/show/{name}")
    public Result showField(HttpServletRequest request, HttpSession session, @PathVariable("name") String name) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            return Result.error(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        webSiteService.updateShowField(name);
        return Result.ok("更新成功!");
    }


    @PostMapping("/update/pwd")
    public Result updatePwd(HttpServletRequest request, HttpSession session, @RequestBody UpdPassword updPassword) {
        AuthContext context = getContext(request);

        return Result.ok("保存网站信息成功!");
    }


}
