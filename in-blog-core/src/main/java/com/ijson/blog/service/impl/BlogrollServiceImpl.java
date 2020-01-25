package com.ijson.blog.service.impl;

import com.ijson.blog.dao.BlogrollDao;
import com.ijson.blog.dao.entity.BlogrollEntity;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.BlogrollService;
import com.ijson.mongo.generator.util.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:28 PM
 */
@Service
public class BlogrollServiceImpl implements BlogrollService {

    @Autowired
    private BlogrollDao blogrollDao;

    @Override
    public BlogrollEntity findInternalById(String id) {
        return blogrollDao.findInternalById(id);
    }

    @Override
    public BlogrollEntity edit(AuthContext context, BlogrollEntity entity) {
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return blogrollDao.update(entity);
    }

    @Override
    public BlogrollEntity create(AuthContext context, BlogrollEntity entity) {
        entity.setId(ObjectId.getId());
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setEnable(true);
        entity.setCreatedBy(context.getId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return blogrollDao.create(entity);
    }

    @Override
    public void enable(String id, boolean enable, AuthContext context) {
        blogrollDao.enable(id, enable, context.getId());
    }

    @Override
    public void delete(String id) {
        blogrollDao.delete(id);
    }
}
