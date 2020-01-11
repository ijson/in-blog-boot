package com.ijson.blog.service.impl;

import com.ijson.blog.dao.RoleDao;
import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.service.AuthService;
import com.ijson.blog.service.UserService;
import com.ijson.blog.service.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/14 9:21 AM
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserService userService;


    /**
     * 非高频操作
     *
     * @return
     */
    @Override
    public LoginUser getAuthByUserId(String ename) {

        UserEntity user = userService.findUserByEname(ename, null, null);
        LoginUser loginUser = new LoginUser();
        loginUser.setCname(user.getCname());
        loginUser.setEname(user.getEname());
        loginUser.setUserId(user.getId());


        return loginUser;
    }
}
