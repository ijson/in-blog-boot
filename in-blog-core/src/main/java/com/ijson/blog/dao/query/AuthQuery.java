package com.ijson.blog.dao.query;

import com.ijson.blog.model.Constant;
import lombok.Data;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 11:51 PM
 */
@Data
public class AuthQuery {

    private String id;

    private String ename;

    private String cname;

    private String path;

    private Boolean enable;

    private Constant.MenuType menuType;
}

