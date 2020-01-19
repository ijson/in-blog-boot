package com.ijson.blog.dao.impl;

import com.google.common.base.Strings;
import com.ijson.blog.dao.RoleDao;
import com.ijson.blog.dao.entity.RoleEntity;
import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.mongo.generator.util.ObjectId;
import com.ijson.mongo.support.AbstractDao;
import org.apache.commons.collections.CollectionUtils;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/14 1:48 AM
 */
@Component
public class RoleDaoImpl extends AbstractDao<RoleEntity> implements RoleDao {

    @Override
    public RoleEntity create(RoleEntity entity) {
        if (!Strings.isNullOrEmpty(entity.getId())) {
            return update(entity);
        }
        entity.setId(ObjectId.getId());
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

        if (CollectionUtils.isNotEmpty(entity.getPermission())) {
            operations.set(RoleEntity.Fields.permission, entity.getPermission());
        }

        if (!Strings.isNullOrEmpty(entity.getParentRoleId())) {
            operations.set(RoleEntity.Fields.parentRoleId, entity.getParentRoleId());
        }

        if (!Strings.isNullOrEmpty(entity.getStatus())) {
            operations.set(RoleEntity.Fields.status, entity.getStatus());
        }

        if (!Strings.isNullOrEmpty(entity.getRemark())) {
            operations.set(RoleEntity.Fields.remark, entity.getRemark());
        }

        operations.set(RoleEntity.Fields.lastModifiedTime, System.currentTimeMillis());
        return datastore.findAndModify(query, operations);
    }

    @Override
    public RoleEntity find(String roleId) {
        Query<RoleEntity> query = datastore.createQuery(RoleEntity.class);
        query.field(UserEntity.Fields.id).equal(roleId);
        query.field(UserEntity.Fields.deleted).equal(false);
        query.field(UserEntity.Fields.enable).equal(true);
        return query.get();
    }

    @Override
    public List<RoleEntity> findByRoleIds(Set<String> roleIds) {
        Query<RoleEntity> query = datastore.createQuery(RoleEntity.class);
        query.field(RoleEntity.Fields.id).hasAnyOf(new HashSet<>(roleIds));
        return query.asList();
    }

}
