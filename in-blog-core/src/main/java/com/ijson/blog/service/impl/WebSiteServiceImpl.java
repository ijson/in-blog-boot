package com.ijson.blog.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.dao.ConfigDao;
import com.ijson.blog.dao.entity.ConfigEntity;
import com.ijson.blog.model.Constant;
import com.ijson.blog.service.WebSiteService;
import com.ijson.blog.service.model.info.WebSiteInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/23 11:29 AM
 */
@Slf4j
@Service
public class WebSiteServiceImpl implements WebSiteService {

    @Autowired
    private ConfigDao configDao;

    @Override
    public ConfigEntity updateWebSite(WebSiteInfo post) {
        ConfigEntity entity = configDao.findType(Constant.ConfigType.site);
        if (Objects.isNull(entity)) {
            entity = new ConfigEntity();
            entity.setType(Constant.ConfigType.site);
        }
        entity.setRegRoleId(post.getRegRoleId());
        entity.setSiteName(post.getSiteName());
        return configDao.updateWebSite(entity);
    }


    @Override
    public ConfigEntity updateSwitch(String type) {

        log.info("updateSwitch {}", type);
        ConfigEntity configDaoType = configDao.findType(Constant.ConfigType.switchType);
        if (Objects.isNull(configDaoType)) {
            configDaoType = new ConfigEntity();
            configDaoType.setType(Constant.ConfigType.switchType);
        }
        switch (type) {
            case ConfigEntity.Fields.openReg:
                configDaoType.setOpenReg(!configDaoType.isOpenReg());
                break;
            case ConfigEntity.Fields.openCmt:
                configDaoType.setOpenCmt(!configDaoType.isOpenCmt());
                break;
            case ConfigEntity.Fields.showAdminInfo:
                configDaoType.setShowAdminInfo(!configDaoType.isShowAdminInfo());
                break;
            case ConfigEntity.Fields.dynamicMenu:
                configDaoType.setDynamicMenu(!configDaoType.isDynamicMenu());
                break;
            case ConfigEntity.Fields.openWechatLogin:
                configDaoType.setOpenWechatLogin(!configDaoType.isOpenWechatLogin());
                break;
            case ConfigEntity.Fields.openWeiboLogin:
                configDaoType.setOpenWeiboLogin(!configDaoType.isOpenWeiboLogin());
                break;
            case ConfigEntity.Fields.openQQLogin:
                configDaoType.setOpenQQLogin(!configDaoType.isOpenQQLogin());
                break;
            default:
                type = null;
        }
        if (Strings.isNullOrEmpty(type)) {
            log.error("开关更新异常");
            return null;
        }
        return configDao.updateSwitch(configDaoType);
    }

    @Override
    public ConfigEntity updateShowField(String name) {
        ConfigEntity configDaoType = configDao.findType(Constant.ConfigType.fieldShow);
        if (Objects.isNull(configDaoType)) {
            configDaoType = new ConfigEntity();
            configDaoType.setType(Constant.ConfigType.fieldShow);
            configDaoType.setShowAdminFields(Lists.newArrayList(name));
        }

        List<String> fields = configDaoType.getShowAdminFields();
        if (fields.contains(name)) {
            fields.remove(name);
        } else {
            fields.add(name);
        }
        configDaoType.setShowAdminFields(fields);

        return configDao.updateShowField(configDaoType);
    }

    @Override
    public ConfigEntity findAllConfig() {
        ConfigEntity entity = new ConfigEntity();

        List<ConfigEntity> allType = configDao.findAllType();

        allType.forEach(k -> {

            if (k.getType() == Constant.ConfigType.site) {
                entity.setSiteName(k.getSiteName());
                entity.setRegRoleId(k.getRegRoleId());
            }


            if (k.getType() == Constant.ConfigType.switchType) {
                entity.setOpenReg(k.isOpenReg());
                entity.setOpenCmt(k.isOpenCmt());
                entity.setShowAdminInfo(k.isShowAdminInfo());
                entity.setOpenWechatLogin(k.isOpenWechatLogin());
                entity.setOpenWeiboLogin(k.isOpenWeiboLogin());
                entity.setOpenQQLogin(k.isOpenQQLogin());
                entity.setDynamicMenu(k.isDynamicMenu());
            }

            if (k.getType() == Constant.ConfigType.fieldShow) {
                entity.setShowAdminFields(k.getShowAdminFields());
            }
        });

        return entity;
    }
}
