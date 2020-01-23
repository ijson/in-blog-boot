package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.ConfigEntity;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/23 1:00 AM
 */
public interface ConfigDao {

    ConfigEntity updateWebSite(ConfigEntity entity);


    ConfigEntity updateSwitch(ConfigEntity entity);


    ConfigEntity updateShowField(ConfigEntity entity);
}
