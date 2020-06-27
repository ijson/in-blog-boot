package com.ijson.blog.service.impl;

import com.ijson.blog.dao.CommentDao;
import com.ijson.blog.dao.UserDao;
import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.dao.query.CommentQuery;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.CommentService;
import com.ijson.blog.service.model.info.CommentInfo;
import com.ijson.mongo.generator.util.ObjectId;
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

    @Autowired
    private UserDao userDao;

    @Override
    public CommentEntity create(AuthContext context, CommentEntity entity) {

        entity.setId(ObjectId.getId());
        return commentDao.create(entity);
    }

    @Override
    public void delete(AuthContext context, String id) {
        commentDao.delete(id);
    }

    @Override
    public PageResult<CommentInfo> find(CommentQuery query, Page pageEntity) {
        //获取所有的评论数据
        PageResult<CommentEntity> comments = commentDao.find(query, pageEntity);
        List<CommentInfo> commentInfos = CommentInfo.create(comments.getDataList());
        List<UserEntity> userEntities = userDao.findCnameAndAvatarByIds(commentInfos.stream().map(CommentInfo::getUserId).collect(Collectors.toList()));
        Map<String, UserEntity> userIdInfo = userEntities.stream().collect(Collectors.toMap(kvalue -> kvalue.getId(), vvalue -> vvalue));

        for (CommentInfo commentInfo : commentInfos) {
            UserEntity userEntity = userIdInfo.get(commentInfo.getUserId());
            if(Objects.nonNull(userEntity)){
                commentInfo.setUserCname(userEntity.getCname());
                commentInfo.setUserAvatar(userEntity.getAvatar());
            }else{
                commentInfo.setUserCname("未知用户");
                commentInfo.setUserAvatar("https://data.ijson.net/avatar.jpg");
            }

        }
        PageResult<CommentInfo> commentInfoPageResult = new PageResult<>();
        commentInfoPageResult.setTotal(comments.getTotal());
        commentInfoPageResult.setDataList(commentInfos);
        return commentInfoPageResult;
    }

    @Override
    public List<CommentEntity> findAll(AuthContext context) {
        return null;
    }
}
