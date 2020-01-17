package com.ijson.blog.service;

import com.ijson.blog.dao.entity.PostDraftEntity;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.model.AuthContext;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2020/1/15 12:11 PM
 */
public interface PostDraftService {

    PostDraftEntity createPostDraft(AuthContext context, PostDraftEntity entity);

    PostDraftEntity find(String id);

    PostDraftEntity find(String ename, String shamId);


}


