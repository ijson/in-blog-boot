package com.ijson.blog.service;

import com.ijson.blog.dao.TopicDao;
import com.ijson.blog.dao.entity.TopicEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/22 2:04 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-mongo-datastore.xml")
public class TopicDaoTest {

    @Autowired
    private TopicDao topicDao;


    @Test
    public void updateShamId() {
        List<TopicEntity> allTest = topicDao.findAll();
        for (TopicEntity entity : allTest) {
            topicDao.updateShamIdTest(entity);
        }
    }
}
