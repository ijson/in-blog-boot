package com.ijson.blog.dao.query;

import com.ijson.blog.dao.model.ReplyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/3/5 7:49 PM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentQuery {

    private String id;
    private String ename;
    private String shamId;
    private ReplyType replyType;
}


