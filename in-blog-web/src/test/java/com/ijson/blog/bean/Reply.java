package com.ijson.blog.bean;

import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/20 5:08 PM
 */
@Data
public class Reply extends BaseEntity
{
    private String id;
    private String topicId;
    private String userId;
    private String postId;
    private String content;
}
