package com.ijson.blog.service;

import com.ijson.blog.dao.CommentDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/22 2:04 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-mongo-datastore.xml")
public class ReplyDaoTest {

    @Autowired
    private CommentDao commentDao;

    @Test
    public void updateShamId() {
        long data = commentDao.countAll();
        System.out.println("===>" + data);
    }
}
