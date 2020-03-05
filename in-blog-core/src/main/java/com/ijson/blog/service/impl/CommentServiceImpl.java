package com.ijson.blog.service.impl;

import com.ijson.blog.dao.CommentDao;
import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.query.CommentQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.CommentService;
import com.ijson.mongo.generator.util.ObjectId;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/9 6:13 PM
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao CommentDao;

    @Override
    public CommentEntity findInternalById(String id) {
        return CommentDao.findInternalById(id);
    }

    @Override
    public CommentEntity edit(AuthContext context, CommentEntity entity) {
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return CommentDao.update(entity);
    }

    @Override
    public CommentEntity create(AuthContext context, CommentEntity entity) {
        entity.setId(ObjectId.getId());
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setEnable(true);
        entity.setCreatedBy(context.getId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return CommentDao.create(entity);
    }

    @Override
    public void enable(String id, boolean enable, AuthContext context) {
        CommentDao.enable(id, enable, context.getId());
    }

    @Override
    public void delete(String id) {
        CommentDao.delete(id);
    }

    @Override
    public PageResult<CommentEntity> find(CommentQuery query, Page pageEntity) {
        return CommentDao.find(query, pageEntity);
    }

    @Override
    public List<CommentEntity> findAll() {
        return CommentDao.findAll();
    }


}
