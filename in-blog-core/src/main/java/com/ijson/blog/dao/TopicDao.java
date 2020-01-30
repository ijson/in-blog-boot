package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.TopicEntity;
import com.ijson.blog.dao.query.TopicQuery;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

import java.util.List;

public interface TopicDao {

    TopicEntity createOrUpdate(TopicEntity entity);

    TopicEntity update(TopicEntity entity);

    TopicEntity create(TopicEntity entity);

    TopicEntity subtract(TopicEntity entity);

    TopicEntity find(String id);

    List<TopicEntity> finds(List<String> ids);

    TopicEntity enable(String id, boolean enable, String userId);

    PageResult<TopicEntity> find(TopicQuery query, Page page);

    TopicEntity delete(String id, String userId);

    void delete(String id);

    TopicEntity findByTopicName(String tipicName);

    TopicEntity inc(String id);

    List<TopicEntity> finHotTag();

    List<TopicEntity> findAll();

    void batchIncTopicCount(List<String> removeTopicId);

    void updateShamIdTest(TopicEntity entity);

    TopicEntity findByShamId(String ename, String shamId);


}

