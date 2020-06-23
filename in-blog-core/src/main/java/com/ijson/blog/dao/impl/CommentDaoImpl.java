package com.ijson.blog.dao.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.ijson.blog.dao.CommentDao;
import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.model.ReplyType;
import com.ijson.blog.dao.query.CommentQuery;
import com.ijson.mongo.support.AbstractDao;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import com.mongodb.WriteConcern;
import org.apache.commons.collections.CollectionUtils;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/9 6:10 PM
 */
@Component
public class CommentDaoImpl extends AbstractDao<CommentEntity> implements CommentDao {
    @Override
    public CommentEntity create(CommentEntity entity) {
        datastore.save(entity);
        return entity;
    }


    @Override
    public CommentEntity update(CommentEntity entity) {
        Query<CommentEntity> query = createQuery();
        query.field(CommentEntity.Fields.id).equal(entity.getId());

        UpdateOperations operations = createUpdateOperations();


        if (!Strings.isNullOrEmpty(entity.getContent())) {
            operations.set(CommentEntity.Fields.content, entity.getContent());
        }

        if (Objects.nonNull(entity.getReplyType())) {
            operations.set(CommentEntity.Fields.replyType, entity.getReplyType());
        }

        if (!Strings.isNullOrEmpty(entity.getUserId())) {
            operations.set(CommentEntity.Fields.userId, entity.getUserId());
        }
        operations.set(CommentEntity.Fields.lastModifiedTime, System.currentTimeMillis());
        return datastore.findAndModify(query, operations);
    }


    @Override
    public CommentEntity enable(String id, boolean enable, String userId) {
        Query<CommentEntity> query = datastore.createQuery(CommentEntity.class);
        query.field(CommentEntity.Fields.id).equal(id);
        UpdateOperations<CommentEntity> updateOperations = datastore.createUpdateOperations(CommentEntity.class);
        updateOperations.set(CommentEntity.Fields.enable, enable);
        updateOperations.set(CommentEntity.Fields.lastModifiedBy, userId);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public void delete(String id) {
        datastore.delete(datastore.createQuery(CommentEntity.class).field(CommentEntity.Fields.id).equal(id), WriteConcern.UNACKNOWLEDGED);
    }

    @Override
    public CommentEntity findInternalById(String id) {
        Query<CommentEntity> query = datastore.createQuery(CommentEntity.class);
        query.field(CommentEntity.Fields.id).equal(id);
        return query.get();
    }


    @Override
    public PageResult<CommentEntity> find(CommentQuery iquery, Page page) {
        Query<CommentEntity> query = datastore.createQuery(CommentEntity.class);


        if (!Strings.isNullOrEmpty(iquery.getId())) {
            query.field(CommentEntity.Fields.id).equal(iquery.getId());
        }

        if (Objects.nonNull(iquery.getEname())) {
            query.field(CommentEntity.Fields.ename).equal(iquery.getEname());
        }

        if (Objects.nonNull(iquery.getShamId())) {
            query.field(CommentEntity.Fields.shamId).equal(iquery.getShamId());
        }

        query.field(CommentEntity.Fields.enable).equal(true);


        if (!Strings.isNullOrEmpty(page.getOrderBy())) {
            query.order("-" + page.getOrderBy());//添加排序
        } else {
            query.order("-" + CommentEntity.Fields.createTime);
        }

        if (page.getPageNumber() > 0) {
            query.offset((page.getPageNumber() - 1) * page.getPageSize()).limit(page.getPageSize());
        }

        long totalNum = query.countAll();
        List<CommentEntity> entities = query.asList();


        PageResult<CommentEntity> ret = new PageResult<>();
        ret.setDataList(entities);
        ret.setTotal(totalNum);
        return ret;
    }


    @Override
    public Map<String, Long> findCountByIds(Set<String> ids) {
        Query<CommentEntity> query = createQuery();
        query.field(CommentEntity.Fields.postId).hasAnyOf(new HashSet<>(ids));
        List<CommentEntity> countEntities = query.asList();
        Map<String, Long> rst = Maps.newHashMap();
        if (CollectionUtils.isNotEmpty(countEntities)) {
            for (CommentEntity entity : countEntities) {
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
    public long findPostCount(String userId) {
        Query<CommentEntity> query = datastore.createQuery(CommentEntity.class);
        query.field(CommentEntity.Fields.userId).equal(userId);
        query.field(CommentEntity.Fields.replyType).equal(ReplyType.comment.name());
        return query.countAll();
    }

    @Override
    public long findReplyCount(String userId) {
        Query<CommentEntity> query = datastore.createQuery(CommentEntity.class);
        query.field(CommentEntity.Fields.userId).equal(userId);
        query.field(CommentEntity.Fields.replyType).equal(ReplyType.reply.name());
        return query.countAll();
    }
}
