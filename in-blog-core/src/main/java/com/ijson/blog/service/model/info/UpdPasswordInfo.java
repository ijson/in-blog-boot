package com.ijson.blog.service.model.info;

import lombok.Data;

@Data
public class UpdPasswordInfo {

    private String id;
    private String ename;
    private String oldPassword;
    private String newPassword;
    private String againPassword;
    private String pwdVerCode;
}