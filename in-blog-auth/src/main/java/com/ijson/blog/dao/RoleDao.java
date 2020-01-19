package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.RoleEntity;

import java.util.List;
import java.util.Set;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/14 1:46 AM
 */
public interface RoleDao {
    RoleEntity create(RoleEntity entity);

    RoleEntity update(RoleEntity entity);

    RoleEntity find(String roleId);

    List<RoleEntity> findByRoleIds(Set<String> roleIds);
}
