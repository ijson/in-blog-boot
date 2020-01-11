package com.ijson.blog.bean;

import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/20 5:02 PM
 */
@Data
public class Topic extends BaseEntity {
    private String id;
    private String userId;
    private String moduleId;
    private long postCount;
}
