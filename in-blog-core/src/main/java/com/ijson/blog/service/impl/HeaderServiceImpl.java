package com.ijson.blog.service.impl;

import com.ijson.blog.dao.HeaderDao;
import com.ijson.blog.dao.entity.HeaderEntity;
import com.ijson.blog.dao.query.HeaderQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.HeaderService;
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
public class HeaderServiceImpl implements HeaderService {

    @Autowired
    private HeaderDao headerDao;

    @Override
    public HeaderEntity findInternalById(String id) {
        return headerDao.findInternalById(id);
    }

    @Override
    public HeaderEntity edit(AuthContext context, HeaderEntity entity) {
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return headerDao.update(entity);
    }

    @Override
    public HeaderEntity create(AuthContext context, HeaderEntity entity) {
        entity.setId(ObjectId.getId());
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setEnable(true);
        entity.setCreatedBy(context.getId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return headerDao.create(entity);
    }

    @Override
    public void enable(String id, boolean enable, AuthContext context) {
        headerDao.enable(id, enable, context.getId());
    }

    @Override
    public void delete(String id) {
        headerDao.delete(id);
    }

    @Override
    public PageResult<HeaderEntity> find(HeaderQuery query, Page pageEntity) {
        return headerDao.find(query, pageEntity);
    }

    @Override
    public List<HeaderEntity> findAll() {
        return headerDao.findUseAll();
    }
}
