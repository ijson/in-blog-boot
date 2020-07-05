package com.ijson.blog.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.dao.ConfigDao;
import com.ijson.blog.dao.entity.ConfigEntity;
import com.ijson.blog.model.Constant;
import com.ijson.blog.service.WebSiteService;
import com.ijson.blog.service.model.info.TencentInfo;
import com.ijson.blog.service.model.info.ThemeInfo;
import com.ijson.blog.service.model.info.WebSiteInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @CachePut(value = "site",key = "#post.type")
    @Override
    public ConfigEntity updateWebSite(WebSiteInfo post) {
        ConfigEntity entity = configDao.findType(Constant.ConfigType.site);
        if (Objects.isNull(entity)) {
            entity = new ConfigEntity();
            entity.setType(Constant.ConfigType.site);
        }
        entity.setRegRoleId(post.getRegRoleId());
        entity.setSiteCopyRight(post.getSiteCopyRight());
        entity.setSiteDesc(post.getSiteDesc());
        entity.setSiteName(post.getSiteName());
        entity.setSiteBulletin(post.getSiteBulletin());
        entity.setSponsor(post.getSponsor());
        configDao.updateWebSite(entity);

        //不使用上面的返回结果 原因是只有site类型的数据  而view需要全部
        return findAllConfig("all");
    }

    @Override
    public ConfigEntity updateTencent(TencentInfo info) {
        ConfigEntity entity = configDao.findType(Constant.ConfigType.tencent);
        if (Objects.isNull(entity)) {
            entity = new ConfigEntity();
            entity.setType(Constant.ConfigType.tencent);
        }
        entity.setAppId(info.getAppId());
        entity.setAppKey(info.getAppKey());
        entity.setQqCallBackUrl(info.getQqCallBackUrl());
        return configDao.updateTencent(entity);
    }

    @Override
    public ConfigEntity updateDefaultTheme(ThemeInfo info) {
        ConfigEntity entity = configDao.findType(Constant.ConfigType.theme);
        if (Objects.isNull(entity)) {
            entity = new ConfigEntity();
            entity.setType(Constant.ConfigType.theme);
        }
        entity.setAdminThemeEname(info.getAdminTheme());
        entity.setViewThemeEname(info.getViewTheme());
        return configDao.updateDefaultTheme(entity);
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

    @Cacheable(value = "site")
    @Override
    public ConfigEntity findAllConfig(String type) {
        ConfigEntity entity = new ConfigEntity();

        List<ConfigEntity> allType = configDao.findAllType();

        allType.forEach(k -> {

            if (k.getType() == Constant.ConfigType.site) {
                entity.setSiteName(k.getSiteName());
                entity.setRegRoleId(k.getRegRoleId());
                entity.setSiteDesc(k.getSiteDesc());
                entity.setSiteCopyRight(k.getSiteCopyRight());
                entity.setSiteBulletin(k.getSiteBulletin());
                entity.setSponsor(k.getSponsor());
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

            if (k.getType() == Constant.ConfigType.tencent) {
                entity.setAppId(k.getAppId());
                entity.setAppKey(k.getAppKey());
                entity.setQqCallBackUrl(k.getQqCallBackUrl());
            }

            if (k.getType() == Constant.ConfigType.theme) {
                entity.setAdminThemeEname(k.getAdminThemeEname());
                entity.setViewThemeEname(k.getViewThemeEname());
            }
        });

        if (Strings.isNullOrEmpty(entity.getViewThemeEname())) {
            entity.setViewThemeEname("default");
        }

        return entity;
    }


}
