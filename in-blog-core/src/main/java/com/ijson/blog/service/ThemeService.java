package com.ijson.blog.service;

import com.ijson.blog.dao.entity.ThemeEntity;
import com.ijson.blog.dao.query.ThemeQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:28 PM
 */
public interface ThemeService {

    ThemeEntity findInternalById(String id);

    ThemeEntity edit(AuthContext context, ThemeEntity entity);

    ThemeEntity create(AuthContext context, ThemeEntity entity);

    void enable(String id, boolean enable, AuthContext context);

    void delete(String id);

    PageResult<ThemeEntity> find(ThemeQuery query, Page pageEntity);

    List<ThemeEntity> findAll();

}
