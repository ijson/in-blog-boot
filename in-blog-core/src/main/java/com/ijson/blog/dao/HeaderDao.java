package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.HeaderEntity;
import com.ijson.blog.dao.query.HeaderQuery;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:25 PM
 */
public interface HeaderDao {

    HeaderEntity create(HeaderEntity entity);

    HeaderEntity update(HeaderEntity entity);

    HeaderEntity enable(String id, boolean enable, String userId);

    void delete(String id);

    HeaderEntity findInternalById(String id);

    PageResult<HeaderEntity> find(HeaderQuery iquery, Page page);

    List<HeaderEntity> findUseAll();
}
