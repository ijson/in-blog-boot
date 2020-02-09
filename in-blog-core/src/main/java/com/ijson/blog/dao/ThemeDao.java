package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.ThemeEntity;
import com.ijson.blog.dao.query.ThemeQuery;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:25 PM
 */
public interface ThemeDao {

    ThemeEntity create(ThemeEntity entity);

    ThemeEntity update(ThemeEntity entity);

    ThemeEntity enable(String id, boolean enable, String userId);

    void delete(String id);

    ThemeEntity findInternalById(String id);

    PageResult<ThemeEntity> find(ThemeQuery iquery, Page page);

    List<ThemeEntity> findAll();

}
