package com.ijson.blog.bean;

import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/20 4:27 PM
 */
@Data
public class User extends BaseEntity {
    private String id;
    private String ename;
    private String paasword;
    private String email;
    private String wechat;
    private String weibo;
    private String qq;
    private String editorType;
}

