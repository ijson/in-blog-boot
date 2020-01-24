package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.dao.query.UserQuery;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserDao {

    UserEntity create(UserEntity entity);

    UserEntity update(UserEntity entity);

    UserEntity findById(String id);

    UserEntity findInternalById(String id);

    UserEntity findByEname(String ename);

    PageResult<UserEntity> find(UserQuery query, Page page);

    Map<String,String> batchCnameByIds(Set<String> userIds);

    UserEntity enable(String id, boolean enable, String userId);

    void delete(String id);

    UserEntity delete(String id, String userId);

    Long count();

    UserEntity delete(String id, Boolean deleted, String userId);
}

