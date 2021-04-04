package com.ijson.blog.service;

import com.ijson.blog.dao.entity.RoleEntity;
import com.ijson.blog.dao.query.RoleQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/14 9:26 PM
 */
public interface RoleService {

    RoleEntity findById(String roleId);

    RoleEntity findInternalById(String id);

    RoleEntity edit(AuthContext context, RoleEntity entity);

    RoleEntity create(AuthContext context, RoleEntity entity);

    void enable(String id, boolean enable, AuthContext context);

    void delete(String id);

    PageResult<RoleEntity> find(RoleQuery query, Page pageEntity);

    List<RoleEntity> findByIds(List<String> ids);

    List<RoleEntity> findAll();

    RoleEntity findByEname(String system);
}
