package com.ijson.blog.service.impl;

import com.ijson.blog.dao.RoleDao;
import com.ijson.blog.dao.entity.RoleEntity;
import com.ijson.blog.dao.query.RoleQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.RoleService;
import com.ijson.mongo.generator.util.ObjectId;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/14 9:26 PM
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public RoleEntity findById(String roleId) {
        return roleDao.find(roleId);
    }

    @Override
    public RoleEntity findInternalById(String id) {
        return roleDao.findInternalById(id);
    }

    @Override
    public RoleEntity edit(AuthContext context, RoleEntity entity) {
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return roleDao.update(entity);
    }

    @Override
    public RoleEntity create(AuthContext context, RoleEntity entity) {
        entity.setId(ObjectId.getId());
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setEnable(true);
        entity.setCreatedBy(context.getId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return roleDao.create(entity);
    }

    @Override
    public void enable(String id, boolean enable, AuthContext context) {
        roleDao.enable(id, enable, context.getId());
    }

    @Override
    public void delete(String id) {
        roleDao.delete(id);
    }

    @Override
    public PageResult<RoleEntity> find(RoleQuery query, Page pageEntity) {
        return roleDao.find(query, pageEntity);
    }

    @Override
    public List<RoleEntity> findByIds(List<String> ids) {
        return roleDao.findByIds(ids);
    }

    @Override
    public List<RoleEntity> findAll() {
        return roleDao.findAll();
    }

    @Override
    public RoleEntity findByEname(String ename) {
        return roleDao.findByEname(ename);
    }
}
