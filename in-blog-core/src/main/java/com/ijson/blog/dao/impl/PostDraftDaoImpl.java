package com.ijson.blog.dao.impl;

import com.google.common.base.Strings;
import com.ijson.blog.dao.PostDraftDao;
import com.ijson.blog.dao.entity.PostDraftEntity;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.query.PostQuery;
import com.ijson.mongo.generator.util.ObjectId;
import com.ijson.mongo.support.AbstractDao;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import com.mongodb.WriteConcern;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2020/1/15 12:03 PM
 */
@Component
public class PostDraftDaoImpl extends AbstractDao<PostDraftEntity> implements PostDraftDao {

    @Override
    public PostDraftEntity createOrUpdate(PostDraftEntity entity) {
        if (!Strings.isNullOrEmpty(entity.getId())) {
            if (entity.isCreate()) {
                datastore.save(entity);
                return entity;
            } else {
                entity = findAndModify(entity);
            }
        } else {
            ObjectId id = new ObjectId();
            entity.setId(id.toHexString());
            entity.setShamId(id.getTimestamp() + "");
            datastore.save(entity);
            return entity;
        }
        return entity;
    }

    private PostDraftEntity findAndModify(PostDraftEntity entity) {
        Query<PostDraftEntity> query = createQuery();
        query.field(PostDraftEntity.Fields.id).equal(entity.getId());
        UpdateOperations operations = createUpdateOperations();

        if (!Strings.isNullOrEmpty(entity.getContent())) {
            operations.set(PostDraftEntity.Fields.content, entity.getContent());
        }

        if (!Strings.isNullOrEmpty(entity.getTopicNames())) {
            operations.set(PostDraftEntity.Fields.topicNames, entity.getTopicNames());
        }

        if (!Strings.isNullOrEmpty(entity.getTitle())) {
            operations.set(PostDraftEntity.Fields.title, entity.getTitle());
        }
        operations.set(PostDraftEntity.Fields.lastModifiedTime, System.currentTimeMillis());
        return datastore.findAndModify(query, operations);
    }

    @Override
    public PostDraftEntity find(String id) {
        Query<PostDraftEntity> query = datastore.createQuery(PostDraftEntity.class);
        query.field(PostDraftEntity.Fields.id).equal(id);
        return query.get();
    }

    @Override
    public PostDraftEntity findByShamIdInternal(String ename, String shamId) {
        Query<PostDraftEntity> query = datastore.createQuery(PostDraftEntity.class);
        query.field(PostDraftEntity.Fields.shamId).equal(shamId);
        query.field(PostDraftEntity.Fields.ename).equal(ename);
        return query.get();
    }

    @Override
    public void removeDraft(String draftId) {
        datastore.delete(datastore.createQuery(PostDraftEntity.class).field(PostDraftEntity.Fields.id).equal(draftId), WriteConcern.UNACKNOWLEDGED);
    }

    @Override
    public PageResult<PostDraftEntity> find(PostQuery iquery, Page page, String authorId) {
        Query<PostDraftEntity> query = datastore.createQuery(PostDraftEntity.class);

        if (!Strings.isNullOrEmpty(iquery.getId())) {
            query.field(PostDraftEntity.Fields.id).equal(iquery.getId());
        }

        if (!Strings.isNullOrEmpty(iquery.getTitle())) {
            if (iquery.isLikeTitle()) {
                query.or(query.criteria(PostDraftEntity.Fields.title).containsIgnoreCase(iquery.getTitle()),
                        query.criteria(PostDraftEntity.Fields.content).containsIgnoreCase(iquery.getTitle()));
            } else {
                query.field(PostDraftEntity.Fields.title).equal(iquery.getTitle());
            }
        }

        if (page.getOrderBy() == null) {
            query.order("-" + PostEntity.Fields.lastModifiedTime);//添加排序
        }
        if (page.getPageNumber() > 0) {
            query.offset((page.getPageNumber() - 1) * page.getPageSize()).limit(page.getPageSize());
        }

        if (iquery.isCurrentUser()) {
            query.field(PostDraftEntity.Fields.createdBy).equal(authorId);
        }

        long totalNum = query.countAll();
        List<PostDraftEntity> entities = query.asList();

        PageResult<PostDraftEntity> ret = new PageResult<>();
        ret.setDataList(entities);
        ret.setTotal(totalNum);
        return ret;
    }
}
