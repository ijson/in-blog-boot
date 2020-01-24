package com.ijson.blog.service;

import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.dao.query.UserQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/28 4:15 PM
 */
public interface UserService {

    AuthContext login(String ename, String password);

    UserEntity reg(UserEntity entity);

    UserEntity findUserByEname(String ename, String email, String mobile);

    UserEntity findUserById(String userId);

    UserEntity edit(UserEntity entity);

    Map<String, String> findCnameByIds(Set<String> userIds);

    PageResult<UserEntity> find(UserQuery query, Page page);

    UserEntity enable(String id, boolean enable, String userId);

    UserEntity findInternalById(String id);

    UserEntity delete(String id, Boolean deleted, String userId);

    void delete(String id);
}
