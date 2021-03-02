package com.ijson.blog.bus.listener;

import com.google.common.base.Strings;
import com.google.common.eventbus.Subscribe;
import com.ijson.blog.bus.EventBusListener;
import com.ijson.blog.bus.OperationType;
import com.ijson.blog.bus.event.ViewArticleEvent;
//import com.ijson.framework.util.Md5Util;
import com.ijson.blog.dao.CountDao;
import com.ijson.blog.dao.entity.CountEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 *
 */
@Slf4j
@Component
public class ViewArticleListener implements EventBusListener {

    @Autowired
    private CountDao countDao;

    @Subscribe
    public void viewArticle(ViewArticleEvent event) {
        if (!Strings.isNullOrEmpty(event.getArticleId())) {
            CountEntity countEntity = countDao.findCountById(event.getArticleId());
            if (Objects.isNull(countEntity)) {
                countEntity = CountEntity.create(event.getArticleId());
                countDao.create(countEntity);
            } else {
                countEntity = countDao.createOrUpdate(countEntity);
            }
            log.info("博文:[{}]查看次数", countEntity.getViews());
        }
    }


    @Override
    public OperationType getType() {
        return OperationType.view_article;
    }
}
