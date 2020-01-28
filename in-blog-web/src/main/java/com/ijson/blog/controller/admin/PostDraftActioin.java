package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.annotation.DocDocument;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.PostDraftEntity;
import com.ijson.blog.dao.query.PostQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.BlogCreateException;
import com.ijson.blog.interceptor.LoginInterceptor;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.V2Result;
import com.ijson.blog.service.model.info.PostInfo;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2020/1/15 12:15 PM
 */
@Slf4j
@RestController
@RequestMapping("/draft")
public class PostDraftActioin extends BaseController {


    @DocDocument(name = "博客草稿添加", desc = "控制台执行添加,需要添加topic")
    @PostMapping("/create")
    public Result createPost(HttpServletRequest request, HttpSession session, @RequestBody PostInfo post) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);

        if (Strings.isNullOrEmpty(post.getTitle())) {
            throw new BlogCreateException(BlogBusinessExceptionCode.TITLE_NOT_SET);
        }

        if (!Strings.isNullOrEmpty(post.getId())) {
            PostDraftEntity postDraftEntity = postDraftService.find(post.getId());
            if (Objects.nonNull(postDraftEntity)) {
                return updatePost(request, post);
            }
        }


        // 创建草稿还是编辑草稿,如果是创建,需要记录ename
        String ename = post.getEname();
        if (Strings.isNullOrEmpty(ename)) {
            ename = context.getEname();
        }

        PostDraftEntity entity = PostDraftEntity.create(post.getId(), context.getId(), post.getTitle(), post.getContent(), post.getTopicName(), ename, post.getShamId());
        entity.setCreate(true);
        entity = postDraftService.createPostDraft(context, entity);
        log.info("草稿创建成功,id:{},title:{}", entity.getId(), entity.getTitle());
        return Result.ok("创建草稿成功!");
    }

    private Result updatePost(HttpServletRequest request, @RequestBody PostInfo post) {
        AuthContext context = getContext(request);

        PostDraftEntity newEntity = PostDraftEntity.update(context, post.getId(), post.getTitle(), post.getContent(), post.getTopicName());

        postDraftService.createPostDraft(context, newEntity);


        return Result.ok("更新草稿成功!");
    }


    @PostMapping("/delete/{ename}/{shamId}")
    public Result delete(@PathVariable("ename") String ename, @PathVariable("shamId") String shamId, HttpServletRequest request) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        PostDraftEntity postEntity = postDraftService.find(ename, shamId);
        if (Objects.nonNull(postEntity)) {
            postDraftService.delete(postEntity.getId());
            return Result.ok("删除成功");
        }
        return Result.error("删除异常,草稿不存在或数据存储异常");

    }


    @RequestMapping("/list")
    @ResponseBody
    public V2Result list(Integer page, Integer limit, HttpServletRequest request) {

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


        PostQuery query = new PostQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setLikeTitle(true);
            query.setTitle(keyWord);
        }

        PageResult<PostDraftEntity> result = postDraftService.find(context, query, pageEntity);

        if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getDataList())) {
            return new V2Result<>();
        }

        List<PostDraftEntity> dataList = result.getDataList();
        List<PostInfo> posts = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            posts = PostInfo.postDraftList(result);
        }

        V2Result v2Result = new V2Result();
        v2Result.setCode(0);
        v2Result.setCount(result.getTotal());
        v2Result.setData(posts);
        v2Result.setMsg("");
        return v2Result;
    }


    @RequestMapping("/user/list")
    @ResponseBody
    public V2Result userList(Integer page, Integer limit, HttpServletRequest request) {

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


        PostQuery query = new PostQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setLikeTitle(true);
            query.setTitle(keyWord);
        }

        query.setCurrentUser(true);
        PageResult<PostDraftEntity> result = postDraftService.find(context, query, pageEntity);

        if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getDataList())) {
            return new V2Result<>();
        }

        List<PostDraftEntity> dataList = result.getDataList();
        List<PostInfo> posts = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            posts = PostInfo.postDraftList(result);
        }

        V2Result v2Result = new V2Result();
        v2Result.setCode(0);
        v2Result.setCount(result.getTotal());
        v2Result.setData(posts);
        v2Result.setMsg("");
        return v2Result;
    }
}
