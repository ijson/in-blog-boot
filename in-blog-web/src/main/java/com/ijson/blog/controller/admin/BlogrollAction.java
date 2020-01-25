package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.BlogrollEntity;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.ReplyCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:32 PM
 */
@Slf4j
@Controller
@RequestMapping("/blogroll")
public class BlogrollAction extends BaseController {


    @PostMapping(value = "/addup")
    public Result addOrUpdate(HttpServletRequest request, @RequestBody BlogrollEntity myEntity) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("添加友情链接异常,未获取到当前登入人用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }

        if (Strings.isNullOrEmpty(myEntity.getId())) {
            return createBlogroll(request, myEntity);
        }

        BlogrollEntity entity = blogrollService.findInternalById(myEntity.getId());

        entity.setCname(myEntity.getCname());
        entity.setImgLink(myEntity.getImgLink());
        entity.setLink(myEntity.getLink());

        blogrollService.edit(context, entity);
        return Result.ok("更新成功!");
    }

    private Result createBlogroll(HttpServletRequest request, BlogrollEntity myEntity) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("更新友情链接异常,未获取到当前登入人用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        blogrollService.create(context, myEntity);
        return Result.ok("创建成功!");
    }

    @PostMapping(value = "/enable/{id}")
    public Result enable(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("未获取到当前登入人用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        BlogrollEntity entity = blogrollService.findInternalById(id);

        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.FRIENDSHIP_LINK_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }

        blogrollService.enable(id, !entity.isEnable(), context);
        return Result.ok(entity.isEnable() ? "禁用成功!" : "启用成功!");
    }

    @PostMapping(value = "/delete/{id}")
    public Result delete(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("未获取到当前登入人用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        BlogrollEntity entity = blogrollService.findInternalById(id);

        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.FRIENDSHIP_LINK_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }

        if (entity.isEnable()) {
            log.info("友情链接启用状态无法删除");
            throw new ReplyCreateException(BlogBusinessExceptionCode.ENABLED_STATE_CANNOT_BE_DELETED);
        }

        blogrollService.delete(id);
        return Result.ok("删除成功!");
    }

}