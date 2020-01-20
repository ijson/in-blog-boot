package com.ijson.blog.dao.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.ijson.blog.dao.CountDao;
import com.ijson.blog.dao.entity.CountEntity;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.model.AccessType;
import com.ijson.mongo.generator.util.ObjectId;
import com.ijson.mongo.support.AbstractDao;
import org.apache.commons.collections.CollectionUtils;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/9 4:37 PM
 */
@Component
public class CountDaoImpl extends AbstractDao<CountEntity> implements CountDao {


    @Override
    public CountEntity createOrUpdate(CountEntity entity) {
        if (!Strings.isNullOrEmpty(entity.getId())) {
            entity = findAndModify(entity);
        } else {
            entity.setId(ObjectId.getId());
            entity.setViews(1);
            datastore.save(entity);
            return entity;
        }
        return entity;
    }


    @Override
    public CountEntity inc(CountEntity entity){
        Query<CountEntity> query = createQuery();
        query.field(CountEntity.Fields.id).equal(entity.getId());
        UpdateOperations operations = createUpdateOperations();
        operations.inc(CountEntity.Fields.views);
        return datastore.findAndModify(query, operations);
    }

    @Override
    public List<CountEntity> findHot() {
        Query<CountEntity> query = createQuery();
        query.field(CountEntity.Fields.id).notEqual("web_site_count");
        query.order("-" + CountEntity.Fields.views);
        return query.limit(10).asList();
    }

    @Override
    public CountEntity create(CountEntity entity) {
        entity.setViews(1);
        datastore.save(entity);
        return entity;
    }

    private CountEntity findAndModify(CountEntity entity) {
        Query<CountEntity> query = createQuery();
        query.field(CountEntity.Fields.id).equal(entity.getId());
        UpdateOperations operations = createUpdateOperations();
        operations.inc(CountEntity.Fields.views);
        return datastore.findAndModify(query, operations);
    }


    @Override
    public CountEntity findCountById(String refId) {
        Query<CountEntity> query = createQuery();
        query.field(CountEntity.Fields.id).equal(refId);
        return query.get();
    }


    @Override
    public CountEntity findCountByWebType(String type) {
        Query<CountEntity> query = createQuery();
        query.field(CountEntity.Fields.accessType).equal(AccessType.webSite.name());
        return query.get();
    }

    @Override
    public Map<String, Long> findCountByIds(Set<String> ids) {
        Query<CountEntity> query = createQuery();
        query.field(CountEntity.Fields.id).hasAnyOf(new HashSet<>(ids));
        List<CountEntity> countEntities = query.asList();
        if (CollectionUtils.isNotEmpty(countEntities)) {
            return countEntities
                    .stream()
                    .collect(Collectors.toMap(CountEntity::getId, CountEntity::getViews));
        }
        return Maps.newHashMap();
    }


}
