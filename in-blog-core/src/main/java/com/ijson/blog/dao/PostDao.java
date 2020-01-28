package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.query.PostQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

public interface PostDao {

    PostEntity createOrUpdate(PostEntity entity,boolean updateLastModifiedTime);

    PostEntity find(String id);

    PostEntity findByShamId(String ename,String shamId);

    PostEntity enable(String id, boolean enable, String userId);

    PageResult<PostEntity> find(PostQuery query, Page page,String authorId);

    PostEntity delete(String id, String userId);

    void delete(String id);

    List<PostEntity> findHotPostByIds(List<String> hotIds);

    long count();

    List<PostEntity> getRecentlyPublished();

    PageResult<PostEntity> findPostByTagId(String id,Page page);

    PostEntity incPros(String id);

    Long findPublishTotal(AuthContext context);

    Long findTodayPublishTotal(AuthContext context);

    PostEntity incPros(String ename, String shamId);

    PostEntity enable(String ename, String shamId, boolean enable, String userId);

    PostEntity findByShamIdInternal(String ename, String shamId);

    PostEntity findByDraftId(String id);

    PostEntity audit(String ename, String shamId, Constant.PostStatus status, String reason, String processorId);
}

