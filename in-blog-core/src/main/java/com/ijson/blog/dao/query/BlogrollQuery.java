package com.ijson.blog.dao.query;

import lombok.Data;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 5:07 PM
 */
@Data
public class BlogrollQuery {

    private String id;

    private String cname;

    private String link;

    private String imgLink;

    private Boolean enable;
}
