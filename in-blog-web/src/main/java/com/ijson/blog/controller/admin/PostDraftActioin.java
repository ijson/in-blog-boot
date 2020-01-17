package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.ijson.blog.annotation.DocDocument;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.PostDraftEntity;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.BlogCreateException;
import com.ijson.blog.exception.BlogUpdateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.Post;
import com.ijson.blog.service.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    public Result createPost(HttpServletRequest request, HttpSession session, @RequestBody Post post) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            return Result.error(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        if (!Strings.isNullOrEmpty(post.getId())) {
            PostDraftEntity postDraftEntity = postDraftService.find(post.getId());
            if(Objects.nonNull(postDraftEntity)){
                return updatePost(request, post);
            }
        }

        if (Strings.isNullOrEmpty(post.getTitle())) {
            throw new BlogCreateException(BlogBusinessExceptionCode.TITLE_NOT_SET);
        }

        PostDraftEntity entity = PostDraftEntity.create(post.getId(), context.getId(), post.getTitle(), post.getContent(), post.getTopicName(), context.getEname());
        entity.setCreate(true);
        entity = postDraftService.createPostDraft(context, entity);
        log.info("草稿创建成功,id:{},title:{}", entity.getId(), entity.getTitle());
        return Result.ok("创建草稿成功!");
    }

    private Result updatePost(HttpServletRequest request, @RequestBody Post post) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            return Result.error(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        if (Strings.isNullOrEmpty(post.getId())) {
            throw new BlogUpdateException(BlogBusinessExceptionCode.POST_UPDATE_ID_NOT_FOUND);
        }
        if (Strings.isNullOrEmpty(post.getTitle())) {
            throw new BlogUpdateException(BlogBusinessExceptionCode.TITLE_NOT_SET);
        }

        PostDraftEntity newEntity = PostDraftEntity.update(context, post.getId(), post.getTitle(), post.getContent(), post.getTopicName());

        postDraftService.createPostDraft(context, newEntity);


        return Result.ok("更新草稿成功!");
    }

}
