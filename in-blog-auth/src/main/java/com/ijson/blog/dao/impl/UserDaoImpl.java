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

import java.util.*;
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

        if (!Strings.isNullOrEmpty(entity.getWechatLink())) {
            operations.set(UserEntity.Fields.wechatLink, entity.getWechatLink());
        }

        if (!Strings.isNullOrEmpty(entity.getWeibo())) {
            operations.set(UserEntity.Fields.weibo, entity.getWeibo());
        }

        if (!Strings.isNullOrEmpty(entity.getWeiboLink())) {
            operations.set(UserEntity.Fields.weiboLink, entity.getWeiboLink());
        }

        if (!Strings.isNullOrEmpty(entity.getQq())) {
            operations.set(UserEntity.Fields.qq, entity.getQq());
        }

        if (!Strings.isNullOrEmpty(entity.getMobile())) {
            operations.set(UserEntity.Fields.mobile, entity.getMobile());
        }


        if (!Strings.isNullOrEmpty(entity.getTwitterName())) {
            operations.set(UserEntity.Fields.twitterName, entity.getTwitterName());
        }

        if (!Strings.isNullOrEmpty(entity.getTwitterLink())) {
            operations.set(UserEntity.Fields.twitterLink, entity.getTwitterLink());
        }

        if (!Strings.isNullOrEmpty(entity.getFacebookName())) {
            operations.set(UserEntity.Fields.facebookName, entity.getFacebookName());
        }

        if (!Strings.isNullOrEmpty(entity.getFacebookLink())) {
            operations.set(UserEntity.Fields.facebookLink, entity.getFacebookLink());
        }

        if (!Strings.isNullOrEmpty(entity.getUniversityName())) {
            operations.set(UserEntity.Fields.universityName, entity.getUniversityName());
        }

        if (!Strings.isNullOrEmpty(entity.getUniversityLink())) {
            operations.set(UserEntity.Fields.universityLink, entity.getUniversityLink());
        }

        if (!Strings.isNullOrEmpty(entity.getProfessional())) {
            operations.set(UserEntity.Fields.professional, entity.getProfessional());
        }


        if (!Strings.isNullOrEmpty(entity.getIndexName())) {
            operations.set(UserEntity.Fields.indexName, entity.getIndexName());
        }


        if (Objects.nonNull(entity.getWorkStartTime())) {
            operations.set(UserEntity.Fields.workStartTime, entity.getWorkStartTime());
        }

        if (Objects.nonNull(entity.getWorkEndTime())) {
            operations.set(UserEntity.Fields.workEndTime, entity.getWorkEndTime());
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

        query.field(UserEntity.Fields.deleted).equal(false);

        if(!Strings.isNullOrEmpty(iquery.getCname())){
            query.field(UserEntity.Fields.cname).containsIgnoreCase(iquery.getCname());
        }

        if (page.getOrderBy() != null) {
            query.order("-" + page.getOrderBy());//添加排序
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
}
