package com.ijson.blog.model;

import lombok.Data;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/14 9:21 PM
 */
@Data
public class Permission {

    private String ename;
    private String cname;
    private String path;
    //menu,action
    private PermissionType type;
    private String fatherEname;
    private boolean clickable;

    public static Permission create(String ename, String cname, String path, PermissionType type,String fatherEname,boolean clickable) {
        Permission permission = new Permission();
        permission.setEname(ename);
        permission.setCname(cname);
        permission.setPath(path);
        permission.setType(type);
        permission.setFatherEname(fatherEname);
        permission.setClickable(clickable);
        return permission;
    }
}
