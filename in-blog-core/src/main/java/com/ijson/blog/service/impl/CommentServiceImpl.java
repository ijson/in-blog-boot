package com.ijson.blog.service.impl;

import com.ijson.blog.dao.CommentDao;
import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.query.CommentQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.CommentService;
import com.ijson.blog.service.model.info.Comment;
import com.ijson.mongo.generator.util.ObjectId;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/9 6:13 PM
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public CommentEntity findInternalById(String id) {
        return commentDao.findInternalById(id);
    }

    @Override
    public CommentEntity edit(AuthContext context, CommentEntity entity) {
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return commentDao.update(entity);
    }

    @Override
    public CommentEntity create(AuthContext context, CommentEntity entity) {
        entity.setId(ObjectId.getId());
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setEnable(true);
        entity.setCreatedBy(context.getId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return commentDao.create(entity);
    }

    @Override
    public void enable(String id, boolean enable, AuthContext context) {
        commentDao.enable(id, enable, context.getId());
    }

    @Override
    public void delete(String id) {
        commentDao.delete(id);
    }

    @Override
    public PageResult<CommentEntity> find(CommentQuery query, Page pageEntity) {
        return commentDao.find(query, pageEntity);
    }


    @CachePut(value = "commentCount", key = "#context.id")
    @Override
    public Comment.CommentCount findCount(AuthContext context) {
        // 博文评论数
        long postCount = commentDao.findPostCount(context.getId());

        // 回复评论数
        long replyCount = commentDao.findReplyCount(context.getId());
        return Comment.CommentCount.create(postCount, replyCount);
    }


}
