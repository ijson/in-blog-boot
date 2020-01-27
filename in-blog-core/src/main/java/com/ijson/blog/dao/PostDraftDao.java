package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.PostDraftEntity;
import com.ijson.blog.dao.query.PostQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2020/1/15 12:01 PM
 */
public interface PostDraftDao {

    PostDraftEntity createOrUpdate(PostDraftEntity entity);

    PostDraftEntity find(String id);

    PostDraftEntity findByShamIdInternal(String ename, String shamId);

    void removeDraft(String draftId);

    PageResult<PostDraftEntity> find(PostQuery iquery, Page page,String authorId);
}
