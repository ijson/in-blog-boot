package com.ijson.blog.service;

import com.ijson.blog.dao.entity.AuthEntity;
import com.ijson.blog.dao.query.AuthQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/14 9:10 AM
 */
public interface AuthService {

    AuthEntity findInternalById(String id);

    AuthEntity edit(AuthContext context, AuthEntity entity);

    AuthEntity create(AuthContext context, AuthEntity entity);

    void enable(String id, boolean enable, AuthContext context);

    void delete(String id);

    PageResult<AuthEntity> find(AuthQuery query, Page pageEntity);

    List<AuthEntity> findByIds(List<String> ids);

    List<AuthEntity> findFathers(String fatherId);

    List<AuthEntity> findAll();

    AuthEntity findByEname(String ename);

    List<AuthEntity> findChild(String id);
}
