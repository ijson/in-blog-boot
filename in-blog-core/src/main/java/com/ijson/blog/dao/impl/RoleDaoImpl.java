package com.ijson.blog.dao.impl;

import com.google.common.base.Strings;
import com.ijson.blog.dao.RoleDao;
import com.ijson.blog.dao.entity.RoleEntity;
import com.ijson.blog.dao.query.RoleQuery;
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
import java.util.Objects;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/14 1:48 AM
 */
@Component
public class RoleDaoImpl extends AbstractDao<RoleEntity> implements RoleDao {

    @Override
    public RoleEntity create(RoleEntity entity) {
        datastore.save(entity);
        return entity;
    }


    @Override
    public RoleEntity update(RoleEntity entity) {
        Query<RoleEntity> query = createQuery();
        query.field(RoleEntity.Fields.id).equal(entity.getId());

        UpdateOperations operations = createUpdateOperations();

        if (!Strings.isNullOrEmpty(entity.getCname())) {
            operations.set(RoleEntity.Fields.cname, entity.getCname());
        }

        if (!Strings.isNullOrEmpty(entity.getEname())) {
            operations.set(RoleEntity.Fields.ename, entity.getEname());
        }

        if (CollectionUtils.isNotEmpty(entity.getUserIds())) {
            operations.set(RoleEntity.Fields.userIds, entity.getUserIds());
        }

        if (CollectionUtils.isNotEmpty(entity.getAuthIds())) {
            operations.set(RoleEntity.Fields.authIds, entity.getAuthIds());
        }

        operations.set(RoleEntity.Fields.verify, entity.getVerify());
        operations.set(RoleEntity.Fields.verifyCmt, entity.getVerifyCmt());
        operations.set(RoleEntity.Fields.lastModifiedTime, System.currentTimeMillis());
        return datastore.findAndModify(query, operations);
    }

    @Override
    public RoleEntity find(String roleId) {
        Query<RoleEntity> query = datastore.createQuery(RoleEntity.class);
        query.field(RoleEntity.Fields.id).equal(roleId);
        query.field(RoleEntity.Fields.enable).equal(true);
        return query.get();
    }


    @Override
    public RoleEntity enable(String id, boolean enable, String userId) {
        Query<RoleEntity> query = datastore.createQuery(RoleEntity.class);
        query.field(RoleEntity.Fields.id).equal(id);
        UpdateOperations<RoleEntity> updateOperations = datastore.createUpdateOperations(RoleEntity.class);
        updateOperations.set(RoleEntity.Fields.enable, enable);
        updateOperations.set(RoleEntity.Fields.lastModifiedBy, userId);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public void delete(String id) {
        datastore.delete(datastore.createQuery(RoleEntity.class).field(RoleEntity.Fields.id).equal(id), WriteConcern.UNACKNOWLEDGED);

    }

    @Override
    public RoleEntity findInternalById(String id) {
        Query<RoleEntity> query = datastore.createQuery(RoleEntity.class);
        query.field(RoleEntity.Fields.id).equal(id);
        return query.get();
    }

    @Override
    public PageResult<RoleEntity> find(RoleQuery iquery, Page page) {
        Query<RoleEntity> query = datastore.createQuery(RoleEntity.class);


        if (!Strings.isNullOrEmpty(iquery.getCname())) {
            query.field(RoleEntity.Fields.cname).containsIgnoreCase(iquery.getCname());
        }

        if (!Strings.isNullOrEmpty(iquery.getEname())) {
            query.field(RoleEntity.Fields.ename).equal(iquery.getEname());
        }

        if (Objects.nonNull(iquery.getEnable())) {
            query.field(RoleEntity.Fields.enable).equal(iquery.getEnable());
        }


        if (page.getOrderBy() != null) {
            query.order("-" + page.getOrderBy());//添加排序
        } else {
            query.order("-" + RoleEntity.Fields.createTime);
        }

        if (page.getPageNumber() > 0) {
            query.offset((page.getPageNumber() - 1) * page.getPageSize()).limit(page.getPageSize());
        }

        long totalNum = query.countAll();
        List<RoleEntity> entities = query.asList();

        PageResult<RoleEntity> ret = new PageResult<>();
        ret.setDataList(entities);
        ret.setTotal(totalNum);
        return ret;
    }

    @Override
    public List<RoleEntity> findByIds(List<String> ids) {
        Query<RoleEntity> query = datastore.createQuery(RoleEntity.class);
        query.field(RoleEntity.Fields.enable).equal(true);
        query.field(RoleEntity.Fields.id).hasAnyOf(new HashSet<>(ids));
        return query.asList();
    }

    @Override
    public List<RoleEntity> findAll() {
        Query<RoleEntity> query = datastore.createQuery(RoleEntity.class);
        query.field(RoleEntity.Fields.enable).equal(true);
        return query.asList();
    }

    @Override
    public RoleEntity findByEname(String ename) {
        Query<RoleEntity> query = datastore.createQuery(RoleEntity.class);
        query.field(RoleEntity.Fields.ename).equal(ename);
        query.field(RoleEntity.Fields.enable).equal(true);
        return query.get();
    }

}
