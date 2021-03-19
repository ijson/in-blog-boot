package com.ijson.blog.service;

import com.ijson.blog.InBlogApplicationTests;
import com.ijson.blog.dao.CommentDao;
import com.ijson.rest.proxy.util.JsonUtil;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.Set;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/22 2:04 PM
 */
public class ReplyDaoTest extends InBlogApplicationTests {

    @Autowired
    private CommentDao commentDao;

    @Test
    public void updateShamId() {
        long data = commentDao.countAll();
        System.out.println("===>" + data);
    }

    @Test
    public void queryByPostId() {
        Set<String> aa = Sets.newHashSet(Lists.newArrayList("5efb545a18bc8f4bf703b3c5", "5e319d551df8f46d0b88970d", "5f0db68418bc8f5fac3a751a", "5e3279f01df8f48fdf320b91", "5e3277b31df8f4885cd640fa", "5e319e431df8f46d0b889716", "5e3af163f0867c8aa3cac758", "5e319f031df8f46d8340182e", "5ef9c6de18bc8fe82b6c301f", "5e319d931df8f46d0b889711", "5e319a911df8f46cda0ff954", "5f0dc27518bc8f5fac3a751d", "5d5b5f474051f62d043fc6bd", "5e31a03f1df8f46d9cadbe39", "5e319a8f1df8f46cda0ff953", "5f0dca6f18bc8f5fac3a751e", "5e319b351df8f46ce9c364c5", "5e3eea0f67d3934b85cfd229", "5e3aecc5f0867c89d08233d0", "5f0de06f18bc8fc2c52a088e"));
        Map<String, Long> data = commentDao.findCountByIds(aa);
        System.out.println("===>" + JsonUtil.toJson(data));
    }
}
