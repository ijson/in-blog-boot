package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.ModuleEntity;
import com.ijson.blog.dao.query.ModuleQuery;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

public interface ModuleDao {

    ModuleEntity createOrUpdate(ModuleEntity entity);

    ModuleEntity find(String id);

    ModuleEntity enable(String id, boolean enable, String userId);

    PageResult<ModuleEntity> find(ModuleQuery query, Page page);

    ModuleEntity delete(String id, String userId);

    void delete(String id);
}

