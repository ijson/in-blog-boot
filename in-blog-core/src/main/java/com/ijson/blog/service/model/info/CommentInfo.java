package com.ijson.blog.service.model.info;

import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.model.ReplyType;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/6/24 1:52 上午
 */
@Data
public class CommentInfo {

    private String id;
    private String ename;
    private String shamId;
    private String postId;
    private String content;
    private ReplyType replyType;
    private String userId;
    private String praise;
    private String replyId;

    private List<CommentInfo> childs;

    public static CommentInfo create(CommentEntity entity) {
        CommentInfo info = new CommentInfo();
        info.setId(entity.getId());
        info.setEname(entity.getEname());
        info.setShamId(entity.getShamId());
        info.setPostId(entity.getPostId());
        info.setContent(entity.getContent());
        info.setReplyId(entity.getReplyId());
        info.setReplyType(entity.getReplyType());
        info.setUserId(entity.getUserId());
        info.setPraise(entity.getPraise());

        return info;
    }


    public static List<CommentInfo> create(List<CommentEntity> entity) {
        if (CollectionUtils.isEmpty(entity)) {
            return Lists.newArrayList();
        }
        return entity.stream().map(CommentInfo::create).collect(Collectors.toList());
    }


}
