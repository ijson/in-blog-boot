package com.ijson.blog.service.impl;

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
    public TopicEntity findByTagName(String tagname) {
        return topicDao.findByTagname(tagname);
    }

    @Override
    public void inc(String id) {
        topicDao.inc(id);
    }

    @Override
    public List<TopicEntity> findByIds(List<String> historyTagIds) {
        return topicDao.findByIds(historyTagIds);
    }


    @Override
    public TopicEntity dec(String id) {
        return topicDao.dec(id);
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
