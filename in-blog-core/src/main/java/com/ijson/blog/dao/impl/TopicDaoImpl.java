package com.ijson.blog.dao.impl;

import com.google.common.base.Strings;
import com.ijson.blog.dao.TopicDao;
import com.ijson.blog.dao.entity.TopicEntity;
import com.ijson.blog.dao.query.TopicQuery;
import com.ijson.mongo.generator.util.ObjectId;
import com.ijson.mongo.support.AbstractDao;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import com.mongodb.WriteConcern;
import org.apache.commons.collections.CollectionUtils;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class TopicDaoImpl extends AbstractDao<TopicEntity> implements TopicDao {


    @Override
    public TopicEntity createOrUpdate(TopicEntity entity) {
        if (!Strings.isNullOrEmpty(entity.getId())) {
            entity = findAndModify(entity);
        } else {
            ObjectId id = new ObjectId();
            entity.setId(id.toHexString());
            entity.setShamId(id.getTimestamp() + "");
            datastore.save(entity);
            return entity;
        }
        return entity;
    }

    @Override
    public TopicEntity update(TopicEntity entity) {
        Query<TopicEntity> query = createQuery();
        query.field(TopicEntity.Fields.id).equal(entity.getId());

        UpdateOperations operations = createUpdateOperations();

        if (!Strings.isNullOrEmpty(entity.getTopicName())) {
            operations.set(TopicEntity.Fields.topicName, entity.getTopicName());
        }

        operations.set(TopicEntity.Fields.lastModifiedTime, System.currentTimeMillis());
        return datastore.findAndModify(query, operations);
    }

    @Override
    public TopicEntity create(TopicEntity entity) {
        datastore.save(entity);
        return entity;
    }


    @Override
    public TopicEntity subtract(TopicEntity entity) {
        Query<TopicEntity> query = createQuery();
        query.field(TopicEntity.Fields.id).equal(entity.getId());
        UpdateOperations operations = createUpdateOperations();
        operations.inc(TopicEntity.Fields.postCount, -1);
        return datastore.findAndModify(query, operations);
    }

    private TopicEntity findAndModify(TopicEntity entity) {
        Query<TopicEntity> query = createQuery();
        query.field(TopicEntity.Fields.id).equal(entity.getId());
        UpdateOperations operations = createUpdateOperations();


        operations.set(TopicEntity.Fields.postCount, entity.getPostCount());

        return datastore.findAndModify(query, operations);
    }

    @Override
    public TopicEntity find(String id) {
        if (Strings.isNullOrEmpty(id)) {
            return null;
        }
        Query<TopicEntity> query = datastore.createQuery(TopicEntity.class);
        query.field(TopicEntity.Fields.id).equal(id);
        return query.get();
    }


    @Override
    public List<TopicEntity> finds(List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return null;
        }
        Query<TopicEntity> query = datastore.createQuery(TopicEntity.class);
        query.field(TopicEntity.Fields.id).hasAnyOf(new HashSet<>(ids));
        query.field(TopicEntity.Fields.enable).equal(true);
        return query.asList();
    }

    @Override
    public TopicEntity enable(String id, boolean enable, String userId) {
        Query<TopicEntity> query = datastore.createQuery(TopicEntity.class);
        query.field(TopicEntity.Fields.id).equal(id);
        UpdateOperations<TopicEntity> updateOperations = datastore.createUpdateOperations(TopicEntity.class);
        updateOperations.set(TopicEntity.Fields.enable, enable);
        updateOperations.set(TopicEntity.Fields.lastModifiedBy, userId);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public PageResult<TopicEntity> find(TopicQuery iquery, Page page) {
        Query<TopicEntity> query = datastore.createQuery(TopicEntity.class);

        if (!Strings.isNullOrEmpty(iquery.getId())) {
            query.field(TopicEntity.Fields.id).equal(iquery.getId());
        }

        if (!Strings.isNullOrEmpty(iquery.getTopicName())) {
            query.field(TopicEntity.Fields.topicName).containsIgnoreCase(iquery.getTopicName());
        }

        query.field(TopicEntity.Fields.deleted).equal(false);
        if (page.getOrderBy() != null) {
            query.order("-" + page.getOrderBy());//添加排序
        } else {
            query.order("-" + TopicEntity.Fields.createTime);
        }
        if (page.getPageNumber() > 0) {
            query.offset((page.getPageNumber() - 1) * page.getPageSize()).limit(page.getPageSize());
        }

        long totalNum = query.countAll();
        List<TopicEntity> entities = query.asList();

        PageResult<TopicEntity> ret = new PageResult<>();
        ret.setDataList(entities);
        ret.setTotal(totalNum);
        return ret;
    }

    @Override
    public TopicEntity delete(String id, String userId) {
        Query<TopicEntity> query = datastore.createQuery(TopicEntity.class);
        query.field(TopicEntity.Fields.createdBy).equal(userId);
        query.field(TopicEntity.Fields.id).equal(id);
        UpdateOperations<TopicEntity> updateOperations = datastore.createUpdateOperations(TopicEntity.class);
        updateOperations.set(TopicEntity.Fields.lastModifiedBy, userId);
        updateOperations.set(TopicEntity.Fields.deleted, true);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public void delete(String id) {
        datastore.delete(datastore.createQuery(TopicEntity.class).field(TopicEntity.Fields.id).equal(id), WriteConcern.UNACKNOWLEDGED);
    }

    @Override
    public TopicEntity findByTopicName(String tipicName) {
        Query<TopicEntity> query = datastore.createQuery(TopicEntity.class);
        query.field(TopicEntity.Fields.topicName).equal(tipicName);
        return query.get();
    }

    @Override
    public TopicEntity inc(String id) {
        Query<TopicEntity> query = createQuery();
        query.field(TopicEntity.Fields.id).equal(id);
        UpdateOperations operations = createUpdateOperations();
        operations.inc(TopicEntity.Fields.postCount);
        return datastore.findAndModify(query, operations);
    }

    @Override
    public List<TopicEntity> finHotTag() {
        Query<TopicEntity> query = createQuery();
        query.order("-" + TopicEntity.Fields.postCount);
        query.field(TopicEntity.Fields.enable).equal(true);
        return query.limit(10).asList();
    }

    @Override
    public List<TopicEntity> findAll() {
        Query<TopicEntity> query = createQuery();
        query.field(TopicEntity.Fields.postCount).notEqual(0);
        query.field(TopicEntity.Fields.enable).equal(true);
        query.order("-" + TopicEntity.Fields.postCount);
        return query.asList();
    }

    @Override
    public void batchIncTopicCount(List<String> removeTopicId) {
        removeTopicId.forEach(id -> {
            Query<TopicEntity> query = createQuery();
            //query.field(TopicEntity.Fields.id).in(removeTopicId);
            query.field(TopicEntity.Fields.id).equal(id);
            UpdateOperations operations = createUpdateOperations();
            operations.dec(TopicEntity.Fields.postCount);
            datastore.findAndModify(query, operations);
        });

    }

    @Override
    public void updateShamIdTest(TopicEntity entity) {
        Query<TopicEntity> query = createQuery();
        query.field(TopicEntity.Fields.id).equal(entity.getId());
        UpdateOperations operations = createUpdateOperations();
        ObjectId objectId = new ObjectId(entity.getId());
        operations.set(TopicEntity.Fields.shamId, objectId.getTimestamp() + "");
        datastore.findAndModify(query, operations);
    }

    @Override
    public TopicEntity findByShamId(String ename, String shamId) {
        Query<TopicEntity> query = datastore.createQuery(TopicEntity.class);
        query.field(TopicEntity.Fields.shamId).equal(shamId);
        query.field(TopicEntity.Fields.ename).equal(ename);
        query.field(TopicEntity.Fields.enable).equal(true);
        return query.get();
    }


}
