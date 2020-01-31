package com.ijson.blog.dao.impl;

import com.google.common.base.Strings;
import com.ijson.blog.dao.IndexMenuDao;
import com.ijson.blog.dao.entity.IndexMenuEntity;
import com.ijson.blog.dao.query.IndexMenuQuery;
import com.ijson.mongo.support.AbstractDao;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import com.mongodb.WriteConcern;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:27 PM
 */
@Component
public class IndexMenuDaoImpl extends AbstractDao<IndexMenuEntity> implements IndexMenuDao {


    @Override
    public IndexMenuEntity create(IndexMenuEntity entity) {
        datastore.save(entity);
        return entity;
    }


    @Override
    public IndexMenuEntity update(IndexMenuEntity entity) {
        Query<IndexMenuEntity> query = createQuery();
        query.field(IndexMenuEntity.Fields.id).equal(entity.getId());

        UpdateOperations operations = createUpdateOperations();

        if (!Strings.isNullOrEmpty(entity.getCname())) {
            operations.set(IndexMenuEntity.Fields.cname, entity.getCname());
        }

//        if (!Strings.isNullOrEmpty(entity.getEname())) {
//            operations.set(IndexMenuEntity.Fields.ename, entity.getEname());
//        }

        if (Objects.nonNull(entity.getOrder())) {
            operations.set(IndexMenuEntity.Fields.order, entity.getOrder());
        }

        if (!Strings.isNullOrEmpty(entity.getPath())) {
            operations.set(IndexMenuEntity.Fields.path, entity.getPath());
        }

        if (!Strings.isNullOrEmpty(entity.getLastModifiedBy())) {
            operations.set(IndexMenuEntity.Fields.lastModifiedBy, entity.getLastModifiedBy());
        }

        operations.set(IndexMenuEntity.Fields.lastModifiedTime, System.currentTimeMillis());
        return datastore.findAndModify(query, operations);
    }


    @Override
    public IndexMenuEntity enable(String id, boolean enable, String userId) {
        Query<IndexMenuEntity> query = datastore.createQuery(IndexMenuEntity.class);
        query.field(IndexMenuEntity.Fields.id).equal(id);
        UpdateOperations<IndexMenuEntity> updateOperations = datastore.createUpdateOperations(IndexMenuEntity.class);
        updateOperations.set(IndexMenuEntity.Fields.enable, enable);
        updateOperations.set(IndexMenuEntity.Fields.lastModifiedBy, userId);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public void delete(String id) {
        datastore.delete(datastore.createQuery(IndexMenuEntity.class).field(IndexMenuEntity.Fields.id).equal(id), WriteConcern.UNACKNOWLEDGED);
    }

    @Override
    public IndexMenuEntity findInternalById(String id) {
        Query<IndexMenuEntity> query = datastore.createQuery(IndexMenuEntity.class);
        query.field(IndexMenuEntity.Fields.id).equal(id);
        return query.get();
    }


    @Override
    public PageResult<IndexMenuEntity> find(IndexMenuQuery iquery, Page page) {
        Query<IndexMenuEntity> query = datastore.createQuery(IndexMenuEntity.class);


        if (!Strings.isNullOrEmpty(iquery.getCname())) {
            query.field(IndexMenuEntity.Fields.cname).containsIgnoreCase(iquery.getCname());
        }

        if (!Strings.isNullOrEmpty(iquery.getEname())) {
            query.field(IndexMenuEntity.Fields.ename).equal(iquery.getEname());
        }

        if (Objects.nonNull(iquery.getEnable())) {
            query.field(IndexMenuEntity.Fields.enable).equal(iquery.getEnable());
        }


        if (page.getOrderBy() != null) {
            query.order("-" + page.getOrderBy());//添加排序
        } else {
            query.order("-" + IndexMenuEntity.Fields.createTime);
        }

        if (page.getPageNumber() > 0) {
            query.offset((page.getPageNumber() - 1) * page.getPageSize()).limit(page.getPageSize());
        }

        long totalNum = query.countAll();
        List<IndexMenuEntity> entities = query.asList();

        PageResult<IndexMenuEntity> ret = new PageResult<>();
        ret.setDataList(entities);
        ret.setTotal(totalNum);
        return ret;
    }

    @Override
    public List<IndexMenuEntity> findUseAll() {
        Query<IndexMenuEntity> query = datastore.createQuery(IndexMenuEntity.class);
        query.field(IndexMenuEntity.Fields.enable).equal(true);
        return query.asList();
    }
}
