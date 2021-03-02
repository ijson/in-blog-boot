package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.AuthEntity;
import com.ijson.blog.dao.query.AuthQuery;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 11:49 PM
 */
public interface AuthDao {

    AuthEntity create(AuthEntity entity);

    AuthEntity update(AuthEntity entity);

    AuthEntity enable(String id, boolean enable, String userId);

    void delete(String id);

    AuthEntity findInternalById(String id);

    PageResult<AuthEntity> find(AuthQuery iquery, Page page);

    List<AuthEntity> findByIds(List<String> ids);

    List<AuthEntity> findFathers(String fatherId);

    List<AuthEntity> findAll();

    AuthEntity findByEname(String ename);

    List<AuthEntity> findChild(String id);
}
