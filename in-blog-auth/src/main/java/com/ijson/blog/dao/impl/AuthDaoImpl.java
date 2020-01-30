package com.ijson.blog.dao.impl;

import com.google.common.base.Strings;
import com.ijson.blog.dao.AuthDao;
import com.ijson.blog.dao.entity.AuthEntity;
import com.ijson.blog.dao.query.AuthQuery;
import com.ijson.mongo.support.AbstractDao;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import com.mongodb.WriteConcern;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 11:52 PM
 */
@Component
public class AuthDaoImpl extends AbstractDao<AuthEntity> implements AuthDao {
    @Override
    public AuthEntity create(AuthEntity entity) {
        datastore.save(entity);
        return entity;
    }

    @Override
    public AuthEntity update(AuthEntity entity) {
        Query<AuthEntity> query = createQuery();
        query.field(AuthEntity.Fields.id).equal(entity.getId());

        UpdateOperations operations = createUpdateOperations();

        if (!Strings.isNullOrEmpty(entity.getCname())) {
            operations.set(AuthEntity.Fields.cname, entity.getCname());
        }

        if (!Strings.isNullOrEmpty(entity.getEname())) {
            operations.set(AuthEntity.Fields.ename, entity.getEname());
        }

        if (!Strings.isNullOrEmpty(entity.getPath())) {
            operations.set(AuthEntity.Fields.path, entity.getPath());
        }

        if (!Strings.isNullOrEmpty(entity.getFatherId())) {
            operations.set(AuthEntity.Fields.fatherId, entity.getFatherId());
        }

        if (Objects.nonNull(entity.getMenuType())) {
            operations.set(AuthEntity.Fields.menuType, entity.getMenuType());
        }

        if (Objects.nonNull(entity.getShowMenu())) {
            operations.set(AuthEntity.Fields.showMenu, entity.getShowMenu());
        }

        operations.set(AuthEntity.Fields.order, entity.getOrder());
        operations.set(AuthEntity.Fields.lastModifiedTime, System.currentTimeMillis());
        return datastore.findAndModify(query, operations);
    }

    @Override
    public AuthEntity enable(String id, boolean enable, String userId) {
        Query<AuthEntity> query = datastore.createQuery(AuthEntity.class);
        query.field(AuthEntity.Fields.id).equal(id);
        UpdateOperations<AuthEntity> updateOperations = datastore.createUpdateOperations(AuthEntity.class);
        updateOperations.set(AuthEntity.Fields.enable, enable);
        updateOperations.set(AuthEntity.Fields.lastModifiedBy, userId);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public void delete(String id) {
        datastore.delete(datastore.createQuery(AuthEntity.class).field(AuthEntity.Fields.id).equal(id), WriteConcern.UNACKNOWLEDGED);
    }

    @Override
    public AuthEntity findInternalById(String id) {
        Query<AuthEntity> query = datastore.createQuery(AuthEntity.class);
        query.field(AuthEntity.Fields.id).equal(id);
        return query.get();
    }

    @Override
    public PageResult<AuthEntity> find(AuthQuery iquery, Page page) {
        Query<AuthEntity> query = datastore.createQuery(AuthEntity.class);


        if (!Strings.isNullOrEmpty(iquery.getCname())) {
            query.field(AuthEntity.Fields.cname).containsIgnoreCase(iquery.getCname());
        }

        if (!Strings.isNullOrEmpty(iquery.getEname())) {
            query.field(AuthEntity.Fields.ename).equal(iquery.getEname());
        }


        if (!Strings.isNullOrEmpty(iquery.getPath())) {
            query.field(AuthEntity.Fields.path).containsIgnoreCase(iquery.getPath());
        }

        if (Objects.nonNull(iquery.getMenuType())) {
            query.field(AuthEntity.Fields.menuType).equal(iquery.getMenuType());
        }


        if (Objects.nonNull(iquery.getEnable())) {
            query.field(AuthEntity.Fields.enable).equal(iquery.getEnable());
        }


        if (page.getOrderBy() != null) {
            query.order("-" + page.getOrderBy());//添加排序
        } else {
            query.order("-" + AuthEntity.Fields.createTime);
        }

        if (page.getPageNumber() > 0) {
            query.offset((page.getPageNumber() - 1) * page.getPageSize()).limit(page.getPageSize());
        }

        long totalNum = query.countAll();
        List<AuthEntity> entities = query.asList();

        PageResult<AuthEntity> ret = new PageResult<>();
        ret.setDataList(entities);
        ret.setTotal(totalNum);
        return ret;
    }

    @Override
    public List<AuthEntity> findByIds(List<String> ids) {
        Query<AuthEntity> query = datastore.createQuery(AuthEntity.class);
        query.field(AuthEntity.Fields.enable).equal(true);
        query.field(AuthEntity.Fields.id).hasAnyOf(new HashSet<>(ids));
        return query.asList();
    }

    @Override
    public List<AuthEntity> findFathers(String fatherId) {
        Query<AuthEntity> query = datastore.createQuery(AuthEntity.class);
        query.field(AuthEntity.Fields.enable).equal(true);
        query.field(AuthEntity.Fields.fatherId).equal(fatherId);
        return query.asList();
    }

    @Override
    public List<AuthEntity> findAll() {
        Query<AuthEntity> query = datastore.createQuery(AuthEntity.class);
        query.field(AuthEntity.Fields.enable).equal(true);
        return query.asList();
    }

    @Override
    public AuthEntity findByEname(String ename) {
        Query<AuthEntity> query = datastore.createQuery(AuthEntity.class);
        query.field(AuthEntity.Fields.ename).equal(ename);
        return query.get();
    }

    @Override
    public List<AuthEntity> findChild(String id) {
        Query<AuthEntity> query = datastore.createQuery(AuthEntity.class);
        query.field(AuthEntity.Fields.fatherId).equal(id);
        return query.asList();
    }
}
