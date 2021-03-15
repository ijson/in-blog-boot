package com.ijson.blog.dao;

import com.ijson.BaseTest;
import com.ijson.blog.dao.entity.CountEntity;
import com.ijson.blog.dao.model.AccessType;
import com.ijson.rest.proxy.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class CountDaoTest extends BaseTest {

    @Autowired
    CountDao countDao;

    @Test
    public void aa() {
        List<CountEntity> hot = countDao.findHot();
        log.info(JsonUtil.toJson(hot));
        System.out.println(hot);
    }

    @Test
    public void findCountByWebType() {
        CountEntity data = countDao.findCountByWebType(AccessType.webSite.name());
        log.info(JsonUtil.toJson(data));
        System.out.println(data);
    }
}
