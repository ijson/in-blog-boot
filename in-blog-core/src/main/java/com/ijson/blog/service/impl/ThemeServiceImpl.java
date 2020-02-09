package com.ijson.blog.service.impl;

import com.ijson.blog.dao.ThemeDao;
import com.ijson.blog.dao.entity.ThemeEntity;
import com.ijson.blog.dao.query.ThemeQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.ThemeService;
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
public class ThemeServiceImpl implements ThemeService {

    @Autowired
    private ThemeDao themeDao;

    @Override
    public ThemeEntity findInternalById(String id) {
        return themeDao.findInternalById(id);
    }

    @Override
    public ThemeEntity edit(AuthContext context, ThemeEntity entity) {
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return themeDao.update(entity);
    }

    @Override
    public ThemeEntity create(AuthContext context, ThemeEntity entity) {
        entity.setId(ObjectId.getId());
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setEnable(true);
        entity.setCreatedBy(context.getId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return themeDao.create(entity);
    }

    @Override
    public void enable(String id, boolean enable, AuthContext context) {
        themeDao.enable(id, enable, context.getId());
    }

    @Override
    public void delete(String id) {
        themeDao.delete(id);
    }

    @Override
    public PageResult<ThemeEntity> find(ThemeQuery query, Page pageEntity) {
        return themeDao.find(query, pageEntity);
    }

    @Override
    public List<ThemeEntity> findAll() {
        return themeDao.findAll();
    }


}
