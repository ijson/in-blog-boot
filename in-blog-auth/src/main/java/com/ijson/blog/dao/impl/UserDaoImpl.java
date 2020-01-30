package com.ijson.blog.dao.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.ijson.blog.dao.UserDao;
import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.dao.query.UserQuery;
import com.ijson.mongo.generator.util.ObjectId;
import com.ijson.mongo.support.AbstractDao;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import com.mongodb.WriteConcern;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/10 8:12 PM
 */
@Component
public class UserDaoImpl extends AbstractDao<UserEntity> implements UserDao {

    @Override
    public UserEntity create(UserEntity entity) {
        if (!Strings.isNullOrEmpty(entity.getId())) {
            return update(entity);
        }
        entity.setId(ObjectId.getId());
        datastore.save(entity);
        return entity;
    }

    @Override
    public UserEntity update(UserEntity entity) {
        Query<UserEntity> query = createQuery();
        query.field(UserEntity.Fields.id).equal(entity.getId());

        UpdateOperations operations = createUpdateOperations();

        if (!Strings.isNullOrEmpty(entity.getCname())) {
            operations.set(UserEntity.Fields.cname, entity.getCname());
        }

        if (!Strings.isNullOrEmpty(entity.getPassword())) {
            operations.set(UserEntity.Fields.password, entity.getPassword());
        }

        if (!Strings.isNullOrEmpty(entity.getEmail())) {
            operations.set(UserEntity.Fields.email, entity.getEmail());
        }

        if (!Strings.isNullOrEmpty(entity.getWechat())) {
            operations.set(UserEntity.Fields.wechat, entity.getWechat());
        }

        if (!Strings.isNullOrEmpty(entity.getWeibo())) {
            operations.set(UserEntity.Fields.weibo, entity.getWeibo());
        }


        if (!Strings.isNullOrEmpty(entity.getQq())) {
            operations.set(UserEntity.Fields.qq, entity.getQq());
        }

        if (!Strings.isNullOrEmpty(entity.getMobile())) {
            operations.set(UserEntity.Fields.mobile, entity.getMobile());
        }


        if (!Strings.isNullOrEmpty(entity.getTwitter())) {
            operations.set(UserEntity.Fields.twitter, entity.getTwitter());
        }


        if (!Strings.isNullOrEmpty(entity.getFacebook())) {
            operations.set(UserEntity.Fields.facebook, entity.getFacebook());
        }

        if (!Strings.isNullOrEmpty(entity.getSchool())) {
            operations.set(UserEntity.Fields.school, entity.getSchool());
        }

        if (!Strings.isNullOrEmpty(entity.getSchoolLink())) {
            operations.set(UserEntity.Fields.schoolLink, entity.getSchoolLink());
        }

        if (!Strings.isNullOrEmpty(entity.getProfession())) {
            operations.set(UserEntity.Fields.profession, entity.getProfession());
        }

        if (!Strings.isNullOrEmpty(entity.getRoleId())) {
            operations.set(UserEntity.Fields.roleId, entity.getRoleId());
        }

        if (!Strings.isNullOrEmpty(entity.getRoleCname())) {
            operations.set(UserEntity.Fields.roleCname, entity.getRoleCname());
        }

        if (Objects.nonNull(entity.getBeginJobTime())) {
            operations.set(UserEntity.Fields.beginJobTime, entity.getBeginJobTime());
        }

        if (Objects.nonNull(entity.getEndJobTime())) {
            operations.set(UserEntity.Fields.endJobTime, entity.getEndJobTime());
        }

        operations.set(UserEntity.Fields.lastModifiedTime, System.currentTimeMillis());
        return datastore.findAndModify(query, operations);
    }

    @Override
    public UserEntity findById(String id) {
        Query<UserEntity> query = datastore.createQuery(UserEntity.class);
        query.field(UserEntity.Fields.id).equal(id);
        query.field(UserEntity.Fields.deleted).equal(false);
        query.field(UserEntity.Fields.enable).equal(true);
        return query.get();
    }

    @Override
    public UserEntity findInternalById(String id) {
        Query<UserEntity> query = datastore.createQuery(UserEntity.class);
        query.field(UserEntity.Fields.id).equal(id);
        return query.get();
    }

    @Override
    public UserEntity findByEname(String ename) {
        Query<UserEntity> query = datastore.createQuery(UserEntity.class);
        query.field(UserEntity.Fields.ename).equal(ename);
        query.field(UserEntity.Fields.deleted).equal(false);
        query.field(UserEntity.Fields.enable).equal(true);
        return query.get();
    }

    @Override
    public PageResult<UserEntity> find(UserQuery iquery, Page page) {
        Query<UserEntity> query = datastore.createQuery(UserEntity.class);


        if (!Strings.isNullOrEmpty(iquery.getCname())) {
            query.field(UserEntity.Fields.cname).containsIgnoreCase(iquery.getCname());
        }

        if (Objects.nonNull(iquery.getDeleted())) {
            query.field(UserEntity.Fields.deleted).equal(iquery.getDeleted());
        } else {
            query.field(UserEntity.Fields.deleted).equal(false);
        }

        if (page.getOrderBy() != null) {
            query.order("-" + page.getOrderBy());//添加排序
        } else {
            query.order("-" + UserEntity.Fields.createTime);//添加排序
        }

        if (page.getPageNumber() > 0) {
            query.offset((page.getPageNumber() - 1) * page.getPageSize()).limit(page.getPageSize());
        }

        long totalNum = query.countAll();
        List<UserEntity> entities = query.asList();

        PageResult<UserEntity> ret = new PageResult<>();
        ret.setDataList(entities);
        ret.setTotal(totalNum);
        return ret;
    }

    @Override
    public Map<String, String> batchCnameByIds(Set<String> userIds) {
        Query<UserEntity> query = datastore.createQuery(UserEntity.class);
        query.field(UserEntity.Fields.id).hasAnyOf(userIds);
        query.retrievedFields(true,
                UserEntity.Fields.cname
        );
        List<UserEntity> userEntities = query.asList();
        if (CollectionUtils.isEmpty(userEntities)) {
            return Maps.newHashMap();
        }
        return userEntities.stream().collect(Collectors.toMap(UserEntity::getId, UserEntity::getCname));
    }

    @Override
    public UserEntity enable(String id, boolean enable, String userId) {
        Query<UserEntity> query = datastore.createQuery(UserEntity.class);
        query.field(UserEntity.Fields.id).equal(id);
        UpdateOperations<UserEntity> updateOperations = datastore.createUpdateOperations(UserEntity.class);
        updateOperations.set(UserEntity.Fields.enable, enable);
        updateOperations.set(UserEntity.Fields.lastModifiedBy, userId);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public void delete(String id) {
        datastore.delete(datastore.createQuery(UserEntity.class).field(UserEntity.Fields.id).equal(id), WriteConcern.UNACKNOWLEDGED);
    }

    @Override
    public UserEntity delete(String id, String userId) {
        Query<UserEntity> query = datastore.createQuery(UserEntity.class);
        query.field(UserEntity.Fields.id).equal(id);
        UpdateOperations<UserEntity> updateOperations = datastore.createUpdateOperations(UserEntity.class);
        updateOperations.set(UserEntity.Fields.lastModifiedBy, userId);
        updateOperations.set(UserEntity.Fields.deleted, true);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public Long count() {
        Query<UserEntity> query = createQuery();
        return query.countAll();
    }

    @Override
    public UserEntity delete(String id, Boolean deleted, String userId) {
        Query<UserEntity> query = datastore.createQuery(UserEntity.class);
        query.field(UserEntity.Fields.id).equal(id);
        UpdateOperations<UserEntity> updateOperations = datastore.createUpdateOperations(UserEntity.class);
        updateOperations.set(UserEntity.Fields.deleted, deleted);
        updateOperations.set(UserEntity.Fields.lastModifiedBy, userId);
        return datastore.findAndModify(query, updateOperations);
    }
}
