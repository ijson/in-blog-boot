package com.ijson.blog.service;

import com.ijson.blog.service.model.LoginUser;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/14 9:10 AM
 */
public interface AuthService {

    LoginUser getAuthByUserId(String ename);
}
