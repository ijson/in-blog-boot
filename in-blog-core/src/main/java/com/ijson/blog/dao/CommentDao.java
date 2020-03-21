package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.query.CommentQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.info.Comment;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/3/5 7:51 PM
 */
public interface CommentDao {

    CommentEntity create(CommentEntity entity);

    CommentEntity update(CommentEntity entity);

    CommentEntity enable(String id, boolean enable, String userId);

    void delete(String id);

    CommentEntity findInternalById(String id);

    PageResult<CommentEntity> find(CommentQuery iquery, Page page);

    Map<String,Long> findCountByIds(Set<String> ids);

    long findPostCount(String userId);

    long findReplyCount(String userId);
}
