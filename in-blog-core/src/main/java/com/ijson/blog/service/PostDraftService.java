package com.ijson.blog.service;

import com.ijson.blog.dao.entity.PostDraftEntity;
import com.ijson.blog.dao.query.PostQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2020/1/15 12:11 PM
 */
public interface PostDraftService {

    PostDraftEntity createPostDraft(AuthContext context, PostDraftEntity entity);

    PostDraftEntity find(String id);

    PostDraftEntity find(String ename, String shamId);

    void delete(String id);

    PageResult<PostDraftEntity> find(AuthContext context,PostQuery query, Page page);
}


