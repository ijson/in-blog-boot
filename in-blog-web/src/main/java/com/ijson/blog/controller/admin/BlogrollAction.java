package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.BlogrollEntity;
import com.ijson.blog.dao.query.BlogrollQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.ReplyCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.V2Result;
import com.ijson.blog.service.model.info.BlogrollInfo;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:32 PM
 */
@Slf4j
@RestController
@RequestMapping("/blogroll")
public class BlogrollAction extends BaseController {


    @PostMapping(value = "/addup")
    public Result addOrUpdate(HttpServletRequest request, @RequestBody BlogrollEntity myEntity) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);

        if (Strings.isNullOrEmpty(myEntity.getCname())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.BLOGROLL_DESC_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(myEntity.getLink())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.BLOGROLL_LINK_CANNOT_BE_EMPTY);
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
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);

        blogrollService.create(context, myEntity);
        return Result.ok("创建成功!");
    }

    @PostMapping(value = "/enable/{id}")
    public Result enable(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        BlogrollEntity entity = blogrollService.findInternalById(id);

        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.FRIENDSHIP_LINK_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }

        blogrollService.enable(id, !entity.isEnable(), context);
        return Result.ok(entity.isEnable() ? "禁用成功!" : "启用成功!");
    }

    @PostMapping(value = "/delete/{id}")
    public Result delete(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
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


    @RequestMapping("/list")
    @ResponseBody
    public V2Result<BlogrollInfo> list(Integer page, Integer limit, HttpServletRequest request) {

        AuthContext context = regularCheck(request, Boolean.TRUE, Boolean.TRUE);
        if (Objects.isNull(context)) {
            return new V2Result<>();
        }

        String keyWord = request.getParameter("title");

        Page pageEntity = new Page();
        if (Objects.nonNull(page)) {
            pageEntity.setPageNumber(page);
        }
        if (Objects.nonNull(limit)) {
            pageEntity.setPageSize(limit);
        }


        BlogrollQuery query = new BlogrollQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setCname(keyWord);
        }

        PageResult<BlogrollEntity> result = blogrollService.find(query, pageEntity);

        if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getDataList())) {
            return new V2Result<>();
        }

        List<BlogrollEntity> dataList = result.getDataList();
        List<BlogrollInfo> blogrollInfos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            blogrollInfos = BlogrollInfo.createBlogrollList(dataList);
        }

        V2Result v2Result = new V2Result();
        v2Result.setCode(0);
        v2Result.setCount(result.getTotal());
        v2Result.setData(blogrollInfos);
        v2Result.setMsg("");
        return v2Result;
    }
}