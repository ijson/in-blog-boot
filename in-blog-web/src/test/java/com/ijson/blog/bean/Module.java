package com.ijson.blog.bean;

import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/20 5:03 PM
 */
@Data
public class Module extends BaseEntity {
    private String id;
    private String name;
    private String detail;
    private boolean turn;
    private int rowNo;
}
