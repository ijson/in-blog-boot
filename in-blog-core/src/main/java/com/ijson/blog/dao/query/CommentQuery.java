package com.ijson.blog.dao.query;

import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.model.ReplyType;
import lombok.Data;
import org.mongodb.morphia.annotations.Property;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/3/5 7:49 PM
 */
@Data
public class CommentQuery {

    private String id;
    private String ename;
    private String shamId;

    private String content;

    //被评论人头像
    private String toAvatar;
    //被评论人名称
    private String toCname;
    //被评论人userId
    private String toUserId;


    //评论人头像
    private String fromAvatar;
    //评论人名称
    private String fromCname;
    //评论人userId
    private String fromUserId;

    //评论还是回复
    private ReplyType replyType;

    private String fatherId;

    private String author;

}
