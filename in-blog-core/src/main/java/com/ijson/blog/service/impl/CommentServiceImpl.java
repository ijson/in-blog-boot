package com.ijson.blog.service.impl;

import com.google.common.collect.Lists;
import com.ijson.blog.dao.CommentDao;
import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.model.ReplyType;
import com.ijson.blog.dao.query.CommentQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.CommentService;
import com.ijson.blog.service.model.info.CommentInfo;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/6/23 11:51 下午
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentDao commentDao;

    @Override
    public CommentEntity create(AuthContext context, CommentEntity entity) {

        return commentDao.create(entity);
    }

    @Override
    public void delete(AuthContext context, String id) {
        commentDao.delete(id);
    }

    @Override
    public PageResult<CommentEntity> find(CommentQuery query, Page pageEntity) {
        //获取所有的评论数据
        PageResult<CommentEntity> comments = commentDao.find(query, pageEntity);
        List<CommentInfo> commentInfos = CommentInfo.create(comments.getDataList());


        //获取所有的评论
        List<CommentInfo> allComment = commentInfos.stream().filter(k -> k.getReplyType().equals(ReplyType.comment)).collect(Collectors.toList());
        //所有的回复
        List<CommentInfo> allReply = commentInfos.stream().filter(k -> k.getReplyType().equals(ReplyType.reply)).collect(Collectors.toList());
        //评论相关
        Map<String, CommentInfo> comment = allComment.stream().collect(Collectors.toMap(CommentInfo::getId, v -> v));


        return comments;
    }

    @Override
    public List<CommentEntity> findAll(AuthContext context) {
        return null;
    }
}
