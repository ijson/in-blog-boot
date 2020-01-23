package com.ijson.blog.controller.admin.model;

import lombok.Data;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/15 7:55 PM
 */
@Data
public class UpdPassword {

    private String id;
    private String ename;
    private String oldPassword;
    private String newPassword;
    private String againPassword;
    private String pwdVerCode;
}
