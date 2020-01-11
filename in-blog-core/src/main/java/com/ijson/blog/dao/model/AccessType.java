package com.ijson.blog.dao.model;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/24 3:23 PM
 */
public enum AccessType {
    blog("博客"),
    webSite("网站");

    String desc;

    AccessType(String desc) {
        this.desc = desc;
    }
}
