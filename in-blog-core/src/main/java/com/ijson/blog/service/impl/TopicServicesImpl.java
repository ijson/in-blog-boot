package com.ijson.blog.service.impl;

import com.google.common.collect.Lists;
import com.ijson.blog.dao.TopicDao;
import com.ijson.blog.dao.entity.TopicEntity;
import com.ijson.blog.dao.query.TopicQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.TopicService;
import com.ijson.mongo.generator.util.ObjectId;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/17 1:09 PM
 */
@Service
public class TopicServicesImpl implements TopicService {

    @Autowired
    private TopicDao topicDao;


    @Override
    public List<TopicEntity> findTopicByTopicNameAndIncCount(String topicNames, AuthContext context) {
        String[] topics = topicNames.split(",");
        TopicEntity topicEntity;
        List<TopicEntity> topicEntities = Lists.newArrayList();
        for (String topic : topics) {
            topicEntity = topicDao.findByTopicName(topic);
            if (Objects.isNull(topicEntity)) {
                topicEntity = topicDao.createOrUpdate(TopicEntity.create(topic, context));
            } else {
                topicEntity = topicDao.inc(topicEntity.getId());
            }
            if (Objects.nonNull(topicEntity)) {
                topicEntities.add(topicEntity);
            }
        }
        return topicEntities;
    }

    @Cacheable(value = "hotTopic")
    @Override
    public List<TopicEntity> findHotTag() {
        return topicDao.finHotTag();
    }

    @Override
    public List<TopicEntity> findAll() {
        return topicDao.findAll();
    }

    @Override
    public TopicEntity find(String id) {
        return topicDao.find(id);
    }

    @Override
    public TopicEntity findTopicByShamIdAndEname(String ename, String shamId) {
        return topicDao.findByShamId(ename, shamId);
    }

    @Override
    public void delete(String id) {
        topicDao.delete(id);
    }

    @Override
    public TopicEntity enable(AuthContext context, String id, boolean b) {
        return topicDao.enable(id, b, context.getId());
    }

    @Override
    public PageResult<TopicEntity> find(TopicQuery query, Page pageEntity) {
        return topicDao.find(query, pageEntity);
    }

    @Override
    public TopicEntity edit(AuthContext context, TopicEntity entity) {
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        return topicDao.update(entity);
    }

    @Override
    public TopicEntity create(AuthContext context, TopicEntity entity) {
        ObjectId objectId = ObjectId.get();
        entity.setId(objectId.toHexString());
        entity.setShamId(objectId.getTimestamp() + "");
        entity.setEname(context.getEname());
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setEnable(true);
        entity.setDeleted(false);
        entity.setCreatedBy(context.getId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setLastModifiedBy(context.getId());
        entity.setLastModifiedTime(System.currentTimeMillis());
        return topicDao.create(entity);
    }
}
