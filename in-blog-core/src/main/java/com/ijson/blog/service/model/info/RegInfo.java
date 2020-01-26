package com.ijson.blog.service.model.info;


import lombok.Data;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/1 1:20 PM
 */
@Data
public class RegInfo {

    private String id;
    private String ename;
    private String cname;
    private String password;
    private String email;
    private String mobile;

    private String qq;
    private String wechat;
    private String weibo;
    private String regVerCode;
}
