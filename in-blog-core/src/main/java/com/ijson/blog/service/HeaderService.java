package com.ijson.blog.service;

import com.ijson.blog.dao.entity.HeaderEntity;
import com.ijson.blog.dao.query.HeaderQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:28 PM
 */
public interface HeaderService {

    HeaderEntity findInternalById(String id);

    HeaderEntity edit(AuthContext context, HeaderEntity entity);

    HeaderEntity create(AuthContext context, HeaderEntity entity);

    void enable(String id, boolean enable, AuthContext context);

    void delete(String id);

    PageResult<HeaderEntity> find(HeaderQuery query, Page pageEntity);

    List<HeaderEntity> findAll();
}
