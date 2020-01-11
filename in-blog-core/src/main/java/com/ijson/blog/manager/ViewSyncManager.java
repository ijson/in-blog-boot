package com.ijson.blog.manager;

import com.ijson.blog.dao.CountDao;
import com.ijson.blog.dao.entity.CountEntity;
import com.ijson.blog.dao.entity.PostEntity;
import com.ijson.blog.dao.model.AccessType;
import com.ijson.blog.model.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/11 7:25 PM
 */
@Slf4j
@Component
public class ViewSyncManager {

    @Autowired
    private CountDao countDao;

    //异步保存查看次数
    @Async
    public void syncViewBlog(PostEntity entity) {
        CountEntity countEntity = countDao.findCountById(entity.getId());
        if (Objects.isNull(countEntity)) {
            countEntity = CountEntity.create(entity);
            countDao.create(countEntity);
        } else {
            countEntity = countDao.createOrUpdate(countEntity);
        }
        log.info("博文:[{}]查看次数", countEntity.getViews());
    }

    @Async
    public void syncWebSite() {
        CountEntity countEntity = new CountEntity();
        countEntity.setAccessType(AccessType.webSite);
        countEntity.setId(Constant.WEB_SITE_COUNT);
        CountEntity countById = countDao.findCountById(Constant.WEB_SITE_COUNT);
        if (countById == null) {
            countDao.create(countEntity);
        } else {
            countDao.inc(countEntity);
        }
    }


    @Async
    public void saveViewBlog(PostEntity entity) {
        CountEntity countEntity = CountEntity.create(entity);
        countEntity = countDao.create(countEntity);
        log.info("博文:[{}]查看次数", countEntity.getViews());
    }
}
