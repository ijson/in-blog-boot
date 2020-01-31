package com.ijson.blog.service;

import com.ijson.blog.dao.entity.IndexMenuEntity;
import com.ijson.blog.dao.query.IndexMenuQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:28 PM
 */
public interface IndexMenuService {

    IndexMenuEntity findInternalById(String id);

    IndexMenuEntity edit(AuthContext context, IndexMenuEntity entity);

    IndexMenuEntity create(AuthContext context, IndexMenuEntity entity);

    void enable(String id, boolean enable, AuthContext context);

    void delete(String id);

    PageResult<IndexMenuEntity> find(IndexMenuQuery query, Page pageEntity);

    List<IndexMenuEntity> findAll();
}
