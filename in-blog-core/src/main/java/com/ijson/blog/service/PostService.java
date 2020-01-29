package com.ijson.blog.service;

import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.query.PostQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.blog.service.model.info.WelcomeInfo;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/21 2:28 PM
 */
public interface PostService {

    PostEntity createPost(AuthContext context,PostEntity entity);

    PageResult<PostEntity> find(AuthContext context,PostQuery iquery, Page page);

    List<PostEntity> findHotPostBeforeTen();

    PostEntity findById(String id);

    PostEntity findInternalById(String id);

    PostEntity findByShamId(String ename, String shamId);

    long count();

    List<PostEntity> findRecentlyPublishedBeforeTen();

    PageResult<PostEntity> findPostByTagId(String id, Page page);

    PostEntity incPros(String id);

    long getWebSiteCount(AuthContext context);

    WelcomeInfo getConsoleData();

    WelcomeInfo getUserConsoleData(AuthContext context);

    PostEntity enable(String id, boolean enable, AuthContext context);

    PostEntity delete(String id,AuthContext context);

    PostEntity delete(PostEntity entity, AuthContext context);

    List<String> removeTopic(PostEntity entity, String id, String topicNames);

    List<String> removeTopic(List<String> removeTopicId);

    PostEntity incPros(String ename, String shamId);

    PostEntity enable(String ename, String shamId, boolean enable, AuthContext context);

    PostEntity findByShamIdInternal(String ename, String shamId,boolean includeTopicAncCount);

    PostEntity findByDraftId(String id);

    PostEntity audit(String ename, String shamId, Constant.PostStatus status, String reason, AuthContext context);
}
