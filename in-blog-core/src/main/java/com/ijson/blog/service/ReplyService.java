package com.ijson.blog.service;

import com.ijson.blog.dao.entity.ReplyEntity;
import com.ijson.blog.dao.query.ReplyQuery;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/25 1:57 AM
 */
public interface ReplyService {
    PageResult<ReplyEntity> find(ReplyQuery replyQuery, Page page);

    ReplyEntity save(String content,String shamId,String ename,ReplyEntity entity);
}
