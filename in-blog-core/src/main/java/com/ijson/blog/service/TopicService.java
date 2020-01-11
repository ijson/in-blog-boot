package com.ijson.blog.service;

import com.ijson.blog.dao.entity.TopicEntity;
import com.ijson.blog.model.AuthContext;

import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/17 1:08 PM
 */
public interface TopicService {

    List<TopicEntity> findTopicByTopicNameAndIncCount(String topicName, AuthContext context);

    List<TopicEntity> findHotTag();

    List<TopicEntity> findAll();

    TopicEntity find(String id);

    TopicEntity findTopicByShamIdAndEname(String ename, String shamId);

}
