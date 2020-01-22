package com.ijson.blog.controller.admin.model;

import lombok.Data;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/22 11:59 PM
 */
@Data
public class UserInfo {
    private String id;
    private String cname;
    private String mobile;
    private String email;
    private String universityName;
    private String universityLink;
    private String professional;
    private String startTime;
    private String endTime;

}
