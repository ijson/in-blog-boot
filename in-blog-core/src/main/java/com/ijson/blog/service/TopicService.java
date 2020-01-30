package com.ijson.blog.service;

import com.ijson.blog.dao.entity.TopicEntity;
import com.ijson.blog.dao.query.TopicQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/17 1:08 PM
 */
public interface TopicService {

    TopicEntity edit(AuthContext context, TopicEntity entity);

    TopicEntity create(AuthContext context, TopicEntity myEntity);

    List<TopicEntity> findTopicByTopicNameAndIncCount(String topicName, AuthContext context);

    List<TopicEntity> findHotTag();

    List<TopicEntity> findAll();

    TopicEntity find(String id);

    TopicEntity findTopicByShamIdAndEname(String ename, String shamId);

    void delete(String id);

    TopicEntity enable(AuthContext context, String id, boolean b);

    PageResult<TopicEntity> find(TopicQuery query, Page pageEntity);


}
