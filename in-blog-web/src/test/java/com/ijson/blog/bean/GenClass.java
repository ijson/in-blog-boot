package com.ijson.blog.bean;

import com.google.common.collect.Lists;
import com.ijson.mongo.generator.Bootstrap;
import org.junit.Test;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/20 5:12 PM
 */
public class GenClass {

    @Test
    public void gen() {
        Bootstrap.generator(
                "com.ijson.blog",
                "/Users/cuiyongxu/Desktop",
                "in-blog",
                Lists.newArrayList(User.class,Module.class,Post.class,Reply.class,Topic.class));
    }
}
