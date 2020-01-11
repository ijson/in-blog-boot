package com.ijson.blog.service.impl;

import com.ijson.blog.dao.RoleDao;
import com.ijson.blog.dao.entity.RoleEntity;
import com.ijson.blog.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public RoleEntity create(RoleEntity roleEntity) {
        return roleDao.create(roleEntity);
    }

    @Override
    public RoleEntity findById(String roleId) {
        return roleDao.find(roleId);
    }
}
