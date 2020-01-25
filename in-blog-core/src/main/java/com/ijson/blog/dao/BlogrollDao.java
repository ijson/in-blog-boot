package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.BlogrollEntity;
import com.ijson.blog.dao.query.BlogrollQuery;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:25 PM
 */
public interface BlogrollDao {

    BlogrollEntity create(BlogrollEntity entity);

    BlogrollEntity update(BlogrollEntity entity);

    BlogrollEntity enable(String id, boolean enable, String userId);

    void delete(String id);

    BlogrollEntity findInternalById(String id);

    PageResult<BlogrollEntity> find(BlogrollQuery iquery, Page page);

    List<BlogrollEntity> findUseAll();
}
