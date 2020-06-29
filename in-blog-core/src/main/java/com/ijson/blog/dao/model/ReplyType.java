package com.ijson.blog.dao.model;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/3/4 8:02 PM
 */
public enum ReplyType {
    comment("文章评论"),
    reply("评论回复");

    public String desc;

    ReplyType(String desc) {
        this.desc = desc;
    }
}
