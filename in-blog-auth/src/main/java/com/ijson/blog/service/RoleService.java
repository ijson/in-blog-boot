package com.ijson.blog.service;

import com.ijson.blog.dao.entity.RoleEntity;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/14 9:26 PM
 */
public interface RoleService {

    RoleEntity create(RoleEntity roleEntity);

    RoleEntity findById(String roleId);
}
