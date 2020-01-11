package com.ijson.blog.controller.admin.model;

import lombok.Data;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/15 3:19 PM
 */
@Data
public class BaseUserInfo {
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
