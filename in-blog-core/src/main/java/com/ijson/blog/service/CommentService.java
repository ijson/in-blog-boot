package com.ijson.blog.service;

import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.query.CommentQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.info.Comment;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:28 PM
 */
public interface CommentService {

    CommentEntity findInternalById(String id);

    CommentEntity edit(AuthContext context, CommentEntity entity);

    CommentEntity create(AuthContext context, CommentEntity entity);

    void enable(String id, boolean enable, AuthContext context);

    void delete(String id);

    PageResult<CommentEntity> find(CommentQuery query, Page pageEntity);

    Comment.CommentCount findCount(AuthContext context);
}
