package com.ijson.blog.service.impl;

import com.ijson.blog.dao.IndexMenuDao;
import com.ijson.blog.dao.entity.IndexMenuEntity;
import com.ijson.blog.dao.query.IndexMenuQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.IndexMenuService;
import com.ijson.mongo.generator.util.ObjectId;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:28 PM
 */
@Service
public class IndexMenuServiceImpl implements IndexMenuService {

    @Autowired
    private IndexMenuDao indexMenuDao;

    @Override
    public IndexMenuEntity findInternalById(String id) {
        return indexMenuDao.findInternalById(id);
    }

    @Override
    public IndexMenuEntity edit(AuthContext context, IndexMenuEntity entity) {
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return indexMenuDao.update(entity);
    }

    @Override
    public IndexMenuEntity create(AuthContext context, IndexMenuEntity entity) {
        entity.setId(ObjectId.getId());
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setEnable(true);
        entity.setCreatedBy(context.getId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return indexMenuDao.create(entity);
    }

    @Override
    public void enable(String id, boolean enable, AuthContext context) {
        indexMenuDao.enable(id, enable, context.getId());
    }

    @Override
    public void delete(String id) {
        indexMenuDao.delete(id);
    }

    @Override
    public PageResult<IndexMenuEntity> find(IndexMenuQuery query, Page pageEntity) {
        return indexMenuDao.find(query, pageEntity);
    }

    @Override
    public List<IndexMenuEntity> findAll() {
        return indexMenuDao.findUseAll();
    }
}
