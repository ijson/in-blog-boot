package com.ijson.blog.service;

import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.query.CommentQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.info.CommentInfo;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/6/23 11:38 下午
 */
public interface CommentService {

    CommentEntity create(AuthContext context, CommentEntity entity);

    void delete(AuthContext context, String id);

    PageResult<CommentInfo> find(CommentQuery query, Page pageEntity);

    List<CommentEntity> findAll(AuthContext context);
}
