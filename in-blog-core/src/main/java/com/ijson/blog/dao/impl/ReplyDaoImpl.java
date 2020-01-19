package com.ijson.blog.dao.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.ijson.blog.dao.ReplyDao;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.entity.ReplyEntity;
import com.ijson.blog.dao.query.ReplyQuery;
import com.ijson.mongo.generator.util.ObjectId;
import com.ijson.mongo.support.AbstractDao;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import com.mongodb.WriteConcern;
import org.apache.commons.collections.CollectionUtils;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ReplyDaoImpl extends AbstractDao<ReplyEntity> implements ReplyDao {


    @Override
    public ReplyEntity createOrUpdate(ReplyEntity entity) {
        if (!Strings.isNullOrEmpty(entity.getId())) {
            entity = findAndModify(entity);
        } else {
            entity.setId(ObjectId.getId());
            datastore.save(entity);
            return entity;
        }
        return entity;
    }

    private ReplyEntity findAndModify(ReplyEntity entity) {
        Query<ReplyEntity> query = createQuery();
        query.field(ReplyEntity.Fields.id).equal(entity.getId());
        UpdateOperations operations = createUpdateOperations();


        operations.set(ReplyEntity.Fields.id, entity.getId());
        operations.set(ReplyEntity.Fields.topicId, entity.getTopicId());
        operations.set(ReplyEntity.Fields.userId, entity.getUserId());
        operations.set(ReplyEntity.Fields.postId, entity.getPostId());
        operations.set(ReplyEntity.Fields.content, entity.getContent());

        return datastore.findAndModify(query, operations);
    }

    @Override
    public ReplyEntity find(String id) {
        Query<ReplyEntity> query = datastore.createQuery(ReplyEntity.class);
        query.field(ReplyEntity.Fields.id).equal(id);
        ReplyEntity entity = query.get();
        if (!Objects.isNull(entity)) {
            return entity;
        } else {
            throw new RuntimeException("数据不存在或已删除");
        }
    }

    @Override
    public ReplyEntity enable(String id, boolean enable, String userId) {
        Query<ReplyEntity> query = datastore.createQuery(ReplyEntity.class);
        query.field(ReplyEntity.Fields.id).equal(id);
        UpdateOperations<ReplyEntity> updateOperations = datastore.createUpdateOperations(ReplyEntity.class);
        updateOperations.set(ReplyEntity.Fields.enable, enable);
        updateOperations.set(ReplyEntity.Fields.lastModifiedBy, userId);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public PageResult<ReplyEntity> find(ReplyQuery iquery, Page page) {
        Query<ReplyEntity> query = datastore.createQuery(ReplyEntity.class);
        if (!Strings.isNullOrEmpty(iquery.getId())) {
            query.field(ReplyEntity.Fields.id).equal(iquery.getId());
        }

        if (!Strings.isNullOrEmpty(iquery.getPostId())) {
            query.field(ReplyEntity.Fields.postId).equal(iquery.getPostId());
        }

        if (!Strings.isNullOrEmpty(iquery.getEname())) {
            query.field(ReplyEntity.Fields.ename).equal(iquery.getEname());
        }

        if (!Strings.isNullOrEmpty(iquery.getShamId())) {
            query.field(ReplyEntity.Fields.shamId).equal(iquery.getShamId());
        }

        query.field(ReplyEntity.Fields.deleted).equal(false);

        if (page.getOrderBy() != null) {
            query.order("-" + page.getOrderBy());//添加排序
        } else {
            query.order("-" + ReplyEntity.Fields.createTime);
        }
        if (page.getPageNumber() > 0) {
            query.offset((page.getPageNumber() - 1) * page.getPageSize()).limit(page.getPageSize());
        }

        long totalNum = query.countAll();
        List<ReplyEntity> entities = query.asList();

        PageResult<ReplyEntity> ret = new PageResult<>();
        ret.setDataList(entities);
        ret.setTotal(totalNum);
        return ret;
    }

    @Override
    public ReplyEntity delete(String id, String userId) {
        Query<ReplyEntity> query = datastore.createQuery(ReplyEntity.class);
        query.field(ReplyEntity.Fields.createdBy).equal(userId);
        query.field(ReplyEntity.Fields.id).equal(id);
        UpdateOperations<ReplyEntity> updateOperations = datastore.createUpdateOperations(ReplyEntity.class);
        updateOperations.set(ReplyEntity.Fields.lastModifiedBy, userId);
        updateOperations.set(ReplyEntity.Fields.deleted, true);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public void delete(String id) {
        datastore.delete(datastore.createQuery(ReplyEntity.class).field(ReplyEntity.Fields.id).equal(id), WriteConcern.UNACKNOWLEDGED);
    }

    @Override
    public Map<String, Long> findCountByIds(Set<String> ids) {
        Query<ReplyEntity> query = createQuery();
        query.field(ReplyEntity.Fields.postId).hasAnyOf(new HashSet<>(ids));
        List<ReplyEntity> countEntities = query.asList();
        Map<String, Long> rst = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(countEntities)) {
            for (ReplyEntity entity : countEntities) {
                Long value = rst.get(entity.getPostId());
                if (Objects.nonNull(value)) {
                    value++;
                    rst.put(entity.getPostId(), value);
                } else {
                    rst.put(entity.getPostId(), 1L);
                }
            }

        }
        return rst;
    }

    @Override
    public List<ReplyEntity> findCountById(String id) {
        Query<ReplyEntity> query = createQuery();
        query.field(ReplyEntity.Fields.postId).equal(id);
        return query.asList();
    }

    @Override
    public List<ReplyEntity> findAllTest() {
        Query<ReplyEntity> query = createQuery();
        return query.asList();
    }

    @Override
    public void updateShamIdTest(ReplyEntity entity) {
        Query<ReplyEntity> query = createQuery();
        query.field(ReplyEntity.Fields.id).equal(entity.getId());
        UpdateOperations operations = createUpdateOperations();
        ObjectId objectId = new ObjectId(entity.getPostId());
        operations.set(PostEntity.Fields.shamId, objectId.getTimestamp() + "");
        operations.set(PostEntity.Fields.ename, "cuiyongxu");
        datastore.findAndModify(query, operations);
    }

    @Override
    public Long count() {
        Query<ReplyEntity> query = createQuery();
        return query.countAll();
    }
}
