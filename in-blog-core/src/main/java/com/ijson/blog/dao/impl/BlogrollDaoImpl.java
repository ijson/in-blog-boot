package com.ijson.blog.dao.impl;

import com.google.common.base.Strings;
import com.ijson.blog.dao.BlogrollDao;
import com.ijson.blog.dao.entity.BlogrollEntity;
import com.ijson.blog.dao.query.BlogrollQuery;
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
public class BlogrollDaoImpl extends AbstractDao<BlogrollEntity> implements BlogrollDao {


    @Override
    public BlogrollEntity create(BlogrollEntity entity) {
        datastore.save(entity);
        return entity;
    }


    @Override
    public BlogrollEntity update(BlogrollEntity entity) {
        Query<BlogrollEntity> query = createQuery();
        query.field(BlogrollEntity.Fields.id).equal(entity.getId());

        UpdateOperations operations = createUpdateOperations();

        if (!Strings.isNullOrEmpty(entity.getCname())) {
            operations.set(BlogrollEntity.Fields.cname, entity.getCname());
        }

        if (!Strings.isNullOrEmpty(entity.getImgLink())) {
            operations.set(BlogrollEntity.Fields.imgLink, entity.getImgLink());
        }

        if (!Strings.isNullOrEmpty(entity.getLink())) {
            operations.set(BlogrollEntity.Fields.link, entity.getLink());
        }

        if (!Strings.isNullOrEmpty(entity.getLastModifiedBy())) {
            operations.set(BlogrollEntity.Fields.lastModifiedBy, entity.getLastModifiedBy());
        }

        operations.set(BlogrollEntity.Fields.lastModifiedTime, System.currentTimeMillis());
        return datastore.findAndModify(query, operations);
    }


    @Override
    public BlogrollEntity enable(String id, boolean enable, String userId) {
        Query<BlogrollEntity> query = datastore.createQuery(BlogrollEntity.class);
        query.field(BlogrollEntity.Fields.id).equal(id);
        UpdateOperations<BlogrollEntity> updateOperations = datastore.createUpdateOperations(BlogrollEntity.class);
        updateOperations.set(BlogrollEntity.Fields.enable, enable);
        updateOperations.set(BlogrollEntity.Fields.lastModifiedBy, userId);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public void delete(String id) {
        datastore.delete(datastore.createQuery(BlogrollEntity.class).field(BlogrollEntity.Fields.id).equal(id), WriteConcern.UNACKNOWLEDGED);
    }

    @Override
    public BlogrollEntity findInternalById(String id) {
        Query<BlogrollEntity> query = datastore.createQuery(BlogrollEntity.class);
        query.field(BlogrollEntity.Fields.id).equal(id);
        return query.get();
    }


    @Override
    public PageResult<BlogrollEntity> find(BlogrollQuery iquery, Page page) {
        Query<BlogrollEntity> query = datastore.createQuery(BlogrollEntity.class);


        if (!Strings.isNullOrEmpty(iquery.getCname())) {
            query.field(BlogrollEntity.Fields.cname).containsIgnoreCase(iquery.getCname());
        }

        if (!Strings.isNullOrEmpty(iquery.getImgLink())) {
            query.field(BlogrollEntity.Fields.imgLink).containsIgnoreCase(iquery.getImgLink());
        }


        if (!Strings.isNullOrEmpty(iquery.getLink())) {
            query.field(BlogrollEntity.Fields.link).containsIgnoreCase(iquery.getLink());
        }


        if (Objects.nonNull(iquery.getEnable())) {
            query.field(BlogrollEntity.Fields.enable).equal(iquery.getEnable());
        }


        if (page.getOrderBy() != null) {
            query.order("-" + page.getOrderBy());//添加排序
        } else {
            query.order("-" + BlogrollEntity.Fields.createTime);
        }

        if (page.getPageNumber() > 0) {
            query.offset((page.getPageNumber() - 1) * page.getPageSize()).limit(page.getPageSize());
        }

        long totalNum = query.countAll();
        List<BlogrollEntity> entities = query.asList();

        PageResult<BlogrollEntity> ret = new PageResult<>();
        ret.setDataList(entities);
        ret.setTotal(totalNum);
        return ret;
    }

    @Override
    public List<BlogrollEntity> findUseAll() {
        Query<BlogrollEntity> query = datastore.createQuery(BlogrollEntity.class);
        query.field(BlogrollEntity.Fields.enable).equal(true);
        return query.asList();
    }
}
