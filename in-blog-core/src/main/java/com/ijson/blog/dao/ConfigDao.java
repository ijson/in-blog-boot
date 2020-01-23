package com.ijson.blog.dao;

import com.ijson.blog.dao.entity.ConfigEntity;
import com.ijson.blog.model.Constant;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/23 1:00 AM
 */
public interface ConfigDao {

    ConfigEntity updateWebSite(ConfigEntity entity);


    ConfigEntity updateSwitch(ConfigEntity entity);


    ConfigEntity updateShowField(ConfigEntity entity);

    ConfigEntity findType(Constant.ConfigType type);

    List<ConfigEntity> findAllType();
}
