package com.ijson.blog.dao.impl;

import com.google.common.base.Strings;
import com.ijson.blog.dao.ModuleDao;
import com.ijson.blog.dao.entity.ModuleEntity;
import com.ijson.blog.dao.query.ModuleQuery;
import com.ijson.mongo.generator.util.ObjectId;
import com.ijson.mongo.support.AbstractDao;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import com.mongodb.WriteConcern;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ModuleDaoImpl extends AbstractDao<ModuleEntity> implements ModuleDao {


    @Override
    public ModuleEntity createOrUpdate(ModuleEntity entity) {
        if (!Strings.isNullOrEmpty(entity.getId())) {
            entity = findAndModify(entity);
        } else {
            entity.setId(ObjectId.getId());
            datastore.save(entity);
            return entity;
        }
        return entity;
    }

    private ModuleEntity findAndModify(ModuleEntity entity) {
        Query<ModuleEntity> query = createQuery();
        query.field(ModuleEntity.Fields.id).equal(entity.getId());
        UpdateOperations operations = createUpdateOperations();


        operations.set(ModuleEntity.Fields.id, entity.getId());
        operations.set(ModuleEntity.Fields.name, entity.getName());
        operations.set(ModuleEntity.Fields.detail, entity.getDetail());
        operations.set(ModuleEntity.Fields.turn, entity.isTurn());
        operations.set(ModuleEntity.Fields.rowNo, entity.getRowNo());

        return datastore.findAndModify(query, operations);
    }

    @Override
    public ModuleEntity find(String id) {
        Query<ModuleEntity> query = datastore.createQuery(ModuleEntity.class);
        query.field(ModuleEntity.Fields.id).equal(id);
        ModuleEntity entity = query.get();
        if (!Objects.isNull(entity)) {
            return entity;
        } else {
            throw new RuntimeException("数据不存在或已删除");
        }
    }

    @Override
    public ModuleEntity enable(String id, boolean enable, String userId) {
        Query<ModuleEntity> query = datastore.createQuery(ModuleEntity.class);
        query.field(ModuleEntity.Fields.id).equal(id);
        UpdateOperations<ModuleEntity> updateOperations = datastore.createUpdateOperations(ModuleEntity.class);
        updateOperations.set(ModuleEntity.Fields.enable, enable);
        updateOperations.set(ModuleEntity.Fields.lastModifiedBy, userId);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public PageResult<ModuleEntity> find(ModuleQuery iquery, Page page) {
        Query<ModuleEntity> query = datastore.createQuery(ModuleEntity.class);
        if (!Strings.isNullOrEmpty(iquery.getId())) {
            query.field(ModuleEntity.Fields.id).equal(iquery.getId());
        }

        query.field(ModuleEntity.Fields.deleted).equal(false);
        if (page.getOrderBy() != null) {
            query.order("-" + page.getOrderBy());//添加排序
        } else {
            query.order("-" + ModuleEntity.Fields.createTime);
        }
        if (page.getPageNumber() > 0) {
            query.offset((page.getPageNumber() - 1) * page.getPageSize()).limit(page.getPageSize());
        }

        long totalNum = query.countAll();
        List<ModuleEntity> entities = query.asList();

        PageResult<ModuleEntity> ret = new PageResult<>();
        ret.setDataList(entities);
        ret.setTotal(totalNum);
        return ret;
    }

    @Override
    public ModuleEntity delete(String id, String userId) {
        Query<ModuleEntity> query = datastore.createQuery(ModuleEntity.class);
        query.field(ModuleEntity.Fields.createdBy).equal(userId);
        query.field(ModuleEntity.Fields.id).equal(id);
        UpdateOperations<ModuleEntity> updateOperations = datastore.createUpdateOperations(ModuleEntity.class);
        updateOperations.set(ModuleEntity.Fields.lastModifiedBy, userId);
        updateOperations.set(ModuleEntity.Fields.deleted, true);
        return datastore.findAndModify(query, updateOperations);
    }

    @Override
    public void delete(String id) {
        datastore.delete(datastore.createQuery(ModuleEntity.class).field(ModuleEntity.Fields.id).equal(id), WriteConcern.UNACKNOWLEDGED);
    }
}
