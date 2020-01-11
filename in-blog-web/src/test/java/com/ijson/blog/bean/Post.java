package com.ijson.blog.bean;

import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/20 5:00 PM
 */
@Data
public class Post extends BaseEntity {
    private String id;
    private String topicId;
    private String userId;
    private String content;
    private long pros;
    private long cons;
}
