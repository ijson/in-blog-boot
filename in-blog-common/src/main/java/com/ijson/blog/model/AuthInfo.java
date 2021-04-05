package com.ijson.blog.model;

import lombok.Data;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 5:23 PM
 */
@Data
public class AuthInfo {
    private String id;

    private String cname;

    private String ename;

    private String path;

    private Boolean enable;

    private String fatherId;

    private int order;

    private Constant.MenuType menuType;

    private boolean checked;

    private boolean disabled;

    private String currentId;

    private boolean showMenu;

    private String icon;

    public Boolean getShowMenu() {
        return showMenu;
    }

}
