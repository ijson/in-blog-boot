package com.ijson.blog.dao.impl;

import com.google.common.base.Strings;
import com.ijson.blog.dao.HeaderDao;
import com.ijson.blog.dao.entity.HeaderEntity;
import com.ijson.blog.dao.query.HeaderQuery;
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
public class HeaderDaoImpl extends AbstractDao<HeaderEntity> implements HeaderDao {


    @Override
    public HeaderEntity create(HeaderEntity entity) {
        datastore.save(entity);
        return entity;
    }


    @Override
    public HeaderEntity update(HeaderEntity entity) {
        Query<HeaderEntity> query = createQuery();
        query.field(HeaderEntity.Fields.id).equal(entity.getId());

        UpdateOperations operations = createUpdateOperations();

        if (!Strings.isNullOrEmpty(entity.getCname())) {
            operations.set(HeaderEntity.Fields.cname, entity.getCname());
        }

        if (!Strings.isNullOrEmpty(entity.getCode())) {
            operations.set(HeaderEntity.Fields.code, entity.getCode());
        }


        if (!Strings.isNullOrEmpty(entity.getLastModifiedBy())) {
            operations.set(HeaderEntity.Fields.lastModifiedBy, entity.getLastModifiedBy());
        }

        operations.set(HeaderEntity.Fields.lastModifiedTime, System.currentTimeMillis());
        return datastore.findAndModify(query, operations);
    }


    @Override
    public HeaderEntity enable(String id, boolean enable, String userId) {
        Query<HeaderEntity> query = datastore.createQuery(HeaderEntity.class);
        query.field(HeaderEntity.Fields.id).equal(id);
        UpdateOperations<HeaderEntity> updateOperations = datastore.createUpdateOperations(HeaderEntity.class);
        updateOperations.set(HeaderEntity.Fields.enable, enable);
        updateOperations.set(HeaderEntity.Fields.lastModifiedBy, userId);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public void delete(String id) {
        datastore.delete(datastore.createQuery(HeaderEntity.class).field(HeaderEntity.Fields.id).equal(id), WriteConcern.UNACKNOWLEDGED);
    }

    @Override
    public HeaderEntity findInternalById(String id) {
        Query<HeaderEntity> query = datastore.createQuery(HeaderEntity.class);
        query.field(HeaderEntity.Fields.id).equal(id);
        return query.get();
    }


    @Override
    public PageResult<HeaderEntity> find(HeaderQuery iquery, Page page) {
        Query<HeaderEntity> query = datastore.createQuery(HeaderEntity.class);


        if (!Strings.isNullOrEmpty(iquery.getCname())) {
            query.field(HeaderEntity.Fields.cname).containsIgnoreCase(iquery.getCname());
        }


        if (Objects.nonNull(iquery.getEnable())) {
            query.field(HeaderEntity.Fields.enable).equal(iquery.getEnable());
        }


        if (page.getOrderBy() != null) {
            query.order("-" + page.getOrderBy());//添加排序
        } else {
            query.order("-" + HeaderEntity.Fields.createTime);
        }

        if (page.getPageNumber() > 0) {
            query.offset((page.getPageNumber() - 1) * page.getPageSize()).limit(page.getPageSize());
        }

        long totalNum = query.countAll();
        List<HeaderEntity> entities = query.asList();

        PageResult<HeaderEntity> ret = new PageResult<>();
        ret.setDataList(entities);
        ret.setTotal(totalNum);
        return ret;
    }

    @Override
    public List<HeaderEntity> findUseAll() {
        Query<HeaderEntity> query = datastore.createQuery(HeaderEntity.class);
        query.field(HeaderEntity.Fields.enable).equal(true);
        return query.asList();
    }
}
