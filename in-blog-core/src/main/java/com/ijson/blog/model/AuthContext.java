package com.ijson.blog.model;

import com.google.common.base.Strings;
import com.ijson.blog.dao.entity.AuthEntity;
import com.ijson.blog.dao.entity.UserEntity;
import lombok.Data;

import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/17 1:26 PM
 */
@Data
public class AuthContext {
    //userId
    private String id;
    private String ename;
    private String cname;
    private String email;
    private String mobile;
    private String password;
    private String remember;
    private String weibo;
    private String avatar;
    private String roleId;
    private String roleEname;
    private String roleCname;
    private List<Permission> permission;
    private List<String> permissionPath;
    private List<String> permissionEname;
    private List<AuthEntity> auths;
    private RegSourceType regSourceType;
    private String qqOpenId;
    /**
     * 默认为true
     */
    private boolean verify;

    public AuthContext(String id,
                       String ename,
                       String cname,
                       String email,
                       String mobile,
                       String avatar,
                       RegSourceType regSourceType,
                       String qqOpenId) {
        this.id = id;
        this.ename = ename;
        this.cname = cname;
        this.email = email;
        this.mobile = mobile;
        this.avatar = avatar;
        this.regSourceType = regSourceType;
        this.qqOpenId = qqOpenId;

    }


    public AuthContext(UserEntity entity) {
        this.id = entity.getId();
        this.ename = entity.getEname();
        this.cname = entity.getCname();
        this.email = entity.getEmail();
        this.mobile = entity.getMobile();
        this.avatar = entity.getAvatar();
        this.regSourceType = entity.getRegSourceType();
        this.qqOpenId = entity.getQqOpenId();
        this.weibo = entity.getWeibo();

    }

    public AuthContext() {
    }

    public boolean isRemember() {
        if (!Strings.isNullOrEmpty(remember) && "on".equals(remember)) {
            return true;
        }
        return false;
    }

    public static AuthContext systemAuthContext(){
        return new AuthContext();
    }
}
