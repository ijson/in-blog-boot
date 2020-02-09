package com.ijson.blog.dao.impl;

import com.google.common.base.Strings;
import com.ijson.blog.dao.ThemeDao;
import com.ijson.blog.dao.entity.ThemeEntity;
import com.ijson.blog.dao.query.ThemeQuery;
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
 * Created by cuiyongxu on 2020/2/9 6:10 PM
 */
@Component
public class ThemeDaoImpl extends AbstractDao<ThemeEntity> implements ThemeDao {
    @Override
    public ThemeEntity create(ThemeEntity entity) {
        datastore.save(entity);
        return entity;
    }


    @Override
    public ThemeEntity update(ThemeEntity entity) {
        Query<ThemeEntity> query = createQuery();
        query.field(ThemeEntity.Fields.id).equal(entity.getId());

        UpdateOperations operations = createUpdateOperations();

        if (!Strings.isNullOrEmpty(entity.getCname())) {
            operations.set(ThemeEntity.Fields.cname, entity.getCname());
        }

        if (!Strings.isNullOrEmpty(entity.getEname())) {
            operations.set(ThemeEntity.Fields.ename, entity.getEname());
        }

        if (!Strings.isNullOrEmpty(entity.getDesc())) {
            operations.set(ThemeEntity.Fields.desc, entity.getDesc());
        }

        if (!Strings.isNullOrEmpty(entity.getLastModifiedBy())) {
            operations.set(ThemeEntity.Fields.lastModifiedBy, entity.getLastModifiedBy());
        }

        operations.set(ThemeEntity.Fields.lastModifiedTime, System.currentTimeMillis());
        return datastore.findAndModify(query, operations);
    }


    @Override
    public ThemeEntity enable(String id, boolean enable, String userId) {
        Query<ThemeEntity> query = datastore.createQuery(ThemeEntity.class);
        query.field(ThemeEntity.Fields.id).equal(id);
        UpdateOperations<ThemeEntity> updateOperations = datastore.createUpdateOperations(ThemeEntity.class);
        updateOperations.set(ThemeEntity.Fields.enable, enable);
        updateOperations.set(ThemeEntity.Fields.lastModifiedBy, userId);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public void delete(String id) {
        datastore.delete(datastore.createQuery(ThemeEntity.class).field(ThemeEntity.Fields.id).equal(id), WriteConcern.UNACKNOWLEDGED);
    }

    @Override
    public ThemeEntity findInternalById(String id) {
        Query<ThemeEntity> query = datastore.createQuery(ThemeEntity.class);
        query.field(ThemeEntity.Fields.id).equal(id);
        return query.get();
    }


    @Override
    public PageResult<ThemeEntity> find(ThemeQuery iquery, Page page) {
        Query<ThemeEntity> query = datastore.createQuery(ThemeEntity.class);


        if (!Strings.isNullOrEmpty(iquery.getCname())) {
            query.field(ThemeEntity.Fields.cname).containsIgnoreCase(iquery.getCname());
        }

        if (!Strings.isNullOrEmpty(iquery.getEname())) {
            query.field(ThemeEntity.Fields.ename).equal(iquery.getEname());
        }

        if (Objects.nonNull(iquery.getEnable())) {
            query.field(ThemeEntity.Fields.enable).equal(iquery.getEnable());
        }


        if (page.getOrderBy() != null) {
            query.order("-" + page.getOrderBy());//添加排序
        } else {
            query.order("-" + ThemeEntity.Fields.createTime);
        }

        if (page.getPageNumber() > 0) {
            query.offset((page.getPageNumber() - 1) * page.getPageSize()).limit(page.getPageSize());
        }

        long totalNum = query.countAll();
        List<ThemeEntity> entities = query.asList();

        PageResult<ThemeEntity> ret = new PageResult<>();
        ret.setDataList(entities);
        ret.setTotal(totalNum);
        return ret;
    }
}
