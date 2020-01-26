package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.RoleEntity;
import com.ijson.blog.dao.query.RoleQuery;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/14 1:46 AM
 */
public interface RoleDao {

    RoleEntity create(RoleEntity entity);

    RoleEntity update(RoleEntity entity);

    RoleEntity find(String roleId);

    RoleEntity enable(String id, boolean enable, String userId);

    void delete(String id);

    RoleEntity findInternalById(String id);

    PageResult<RoleEntity> find(RoleQuery iquery, Page page);

    List<RoleEntity> findByIds(List<String> ids);

    List<RoleEntity> findAll();

    RoleEntity findByEname(String ename);
}
