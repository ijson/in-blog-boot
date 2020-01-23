package com.ijson.blog.service.impl;

import com.ijson.blog.dao.ConfigDao;
import com.ijson.blog.dao.entity.ConfigEntity;
import com.ijson.blog.service.WebSiteService;
import com.ijson.blog.service.model.WebSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/23 11:29 AM
 */
@Service
public class WebSiteServiceImpl implements WebSiteService {

    @Autowired
    private ConfigDao configDao;

    @Override
    public ConfigEntity updateWebSite(WebSite post) {
        ConfigEntity entity = new ConfigEntity();
        entity.setSiteName(post.getWebSiteName());
        return configDao.updateWebSite(entity);
    }


    @Override
    public ConfigEntity updateSwitch(String type) {

        ConfigEntity configDaoType = configDao.findType(type);

        ConfigEntity entity = new ConfigEntity();

        switch (type) {
            case "openReg":
                entity.setOpenReg(!configDaoType.isOpenReg());
                break;
            case "openCmt":
                break;
            case "showAdminInfo":
                break;
            case "openWechatLogin":
                break;
            case "openWeiboLogin":
                break;
            case "openQQLogin":
                break;
        }

        return null;
    }
}
