package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.IndexMenuEntity;
import com.ijson.blog.dao.query.IndexMenuQuery;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:25 PM
 */
public interface IndexMenuDao {

    IndexMenuEntity create(IndexMenuEntity entity);

    IndexMenuEntity update(IndexMenuEntity entity);

    IndexMenuEntity enable(String id, boolean enable, String userId);

    void delete(String id);

    IndexMenuEntity findInternalById(String id);

    PageResult<IndexMenuEntity> find(IndexMenuQuery iquery, Page page);

    List<IndexMenuEntity> findUseAll();
}
