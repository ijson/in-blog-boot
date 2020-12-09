package com.ijson.blog.dao.impl;

import com.ijson.blog.dao.ConfigDao;
import com.ijson.blog.dao.entity.ConfigEntity;
import com.ijson.blog.model.Constant;
import com.ijson.mongo.support.AbstractDao;
import org.apache.logging.log4j.util.Strings;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/23 1:00 AM
 */
@Component
public class ConfigDaoImpl extends AbstractDao<ConfigEntity> implements ConfigDao {

    @Override
    public ConfigEntity updateWebSite(ConfigEntity entity) {
        Query<ConfigEntity> query = createQuery();
        query.field(ConfigEntity.Fields.type).equal(Constant.ConfigType.site);
        UpdateOperations operations = createUpdateOperations();
        operations.set(ConfigEntity.Fields.siteName, entity.getSiteName());
        operations.set(ConfigEntity.Fields.regRoleId, entity.getRegRoleId());
        operations.set(ConfigEntity.Fields.siteCopyRight, entity.getSiteCopyRight());
        operations.set(ConfigEntity.Fields.siteDesc, entity.getSiteDesc());
        operations.set(ConfigEntity.Fields.siteBulletin, Strings.isEmpty(entity.getSiteBulletin())?"":entity.getSiteBulletin());
        operations.set(ConfigEntity.Fields.sponsor, entity.getSponsor());
        return datastore.findAndModify(query, operations, false, true);
    }

    @Override
    public ConfigEntity updateTencent(ConfigEntity entity) {
        Query<ConfigEntity> query = createQuery();
        query.field(ConfigEntity.Fields.type).equal(Constant.ConfigType.tencent);
        UpdateOperations operations = createUpdateOperations();
        operations.set(ConfigEntity.Fields.appId, entity.getAppId());
        operations.set(ConfigEntity.Fields.appKey, entity.getAppKey());
        operations.set(ConfigEntity.Fields.qqCallBackUrl, entity.getQqCallBackUrl());
        return datastore.findAndModify(query, operations, false, true);
    }

    @Override
    public ConfigEntity updateDefaultTheme(ConfigEntity entity) {
        Query<ConfigEntity> query = createQuery();
        query.field(ConfigEntity.Fields.type).equal(Constant.ConfigType.theme);
        UpdateOperations operations = createUpdateOperations();
        operations.set(ConfigEntity.Fields.adminThemeEname, entity.getAdminThemeEname());
        operations.set(ConfigEntity.Fields.viewThemeEname, entity.getViewThemeEname());
        return datastore.findAndModify(query, operations, false, true);
    }

    @Override
    public ConfigEntity updateSwitch(ConfigEntity entity) {
        Query<ConfigEntity> query = createQuery();
        query.field(ConfigEntity.Fields.type).equal(Constant.ConfigType.switchType);
        UpdateOperations operations = createUpdateOperations();
        operations.set(ConfigEntity.Fields.openReg, entity.isOpenReg());
        operations.set(ConfigEntity.Fields.openCmt, entity.isOpenCmt());
        operations.set(ConfigEntity.Fields.showAdminInfo, entity.isShowAdminInfo());
        operations.set(ConfigEntity.Fields.openWechatLogin, entity.isOpenWechatLogin());
        operations.set(ConfigEntity.Fields.openWeiboLogin, entity.isOpenWeiboLogin());
        operations.set(ConfigEntity.Fields.openQQLogin, entity.isOpenQQLogin());
        operations.set(ConfigEntity.Fields.dynamicMenu, entity.isDynamicMenu());
        return datastore.findAndModify(query, operations, false, true);
    }

    @Override
    public ConfigEntity updateShowField(ConfigEntity entity) {
        Query<ConfigEntity> query = createQuery();
        query.field(ConfigEntity.Fields.type).equal(Constant.ConfigType.fieldShow);
        UpdateOperations operations = createUpdateOperations();
        operations.set(ConfigEntity.Fields.showAdminFields, entity.getShowAdminFields());
        return datastore.findAndModify(query, operations, false, true);
    }

    @Override
    public ConfigEntity findType(Constant.ConfigType type) {
        Query<ConfigEntity> query = datastore.createQuery(ConfigEntity.class);
        query.field(ConfigEntity.Fields.type).equal(type.name());
        return query.get();
    }

    @Override
    public List<ConfigEntity> findAllType() {
        Query<ConfigEntity> query = datastore.createQuery(ConfigEntity.class);
        query.field(ConfigEntity.Fields.type).hasAnyOf(Arrays.asList(Constant.ConfigType.values()));
        return query.asList();
    }
}
