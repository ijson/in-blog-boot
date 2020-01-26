package com.ijson.blog.service;

import com.ijson.blog.dao.entity.BlogrollEntity;
import com.ijson.blog.dao.query.BlogrollQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:28 PM
 */
public interface BlogrollService {

    BlogrollEntity findInternalById(String id);

    BlogrollEntity edit(AuthContext context, BlogrollEntity entity);

    BlogrollEntity create(AuthContext context, BlogrollEntity entity);

    void enable(String id, boolean enable, AuthContext context);

    void delete(String id);

    PageResult<BlogrollEntity> find(BlogrollQuery query, Page pageEntity);

    List<BlogrollEntity> findAll();
}
