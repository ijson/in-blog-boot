package com.ijson.blog.dao.query;

import com.ijson.blog.dao.model.ReplyType;
import lombok.Data;

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

    //评论还是回复
    private ReplyType replyType;
}


