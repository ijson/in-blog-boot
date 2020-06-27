package com.ijson.blog.service.model.info;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.CommentEntity;
import com.ijson.blog.dao.entity.ReplyEntity;
import com.ijson.blog.dao.model.ReplyType;
import com.ijson.blog.model.AuthContext;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
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
    private Long praise = 0L;
    private String replyId;

    private String userCname;
    private String userAvatar;

    private List<CommentInfo> childs;

    private Long createTime;

    private String replyCode;

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
        info.setPraise(Objects.isNull(entity.getPraise()) ? 0L : entity.getPraise());
        info.setCreateTime(entity.getCreateTime());
        return info;
    }


    public static List<CommentInfo> create(List<CommentEntity> entity) {
        if (CollectionUtils.isEmpty(entity)) {
            return Lists.newArrayList();
        }
        return entity.stream().map(CommentInfo::create).collect(Collectors.toList());
    }


    public static CommentEntity formCommentEntity(CommentInfo reply, HttpServletRequest request, AuthContext context) {
        CommentEntity entity = new CommentEntity();
        entity.setUserId(context.getId());
        entity.setPostId(reply.getPostId());
        entity.setContent(reply.getContent());
        entity.setEname(reply.getEname());
        entity.setShamId(reply.getShamId());
        entity.setReplyType(ReplyType.comment);
        entity.setHost(request.getRemoteHost());
        String ua = request.getHeader("User-Agent");
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        OperatingSystem os = userAgent.getOperatingSystem();
        Browser browser = userAgent.getBrowser();
        entity.setUserAgent(ua);
        entity.setOs(os.getName());
        String browserName = browser.getName();
        entity.setBrowse(browserName);
        entity.setLastModifiedBy(context.getId());
        entity.setDeleted(false);
        entity.setEnable(true);
        entity.setCreatedBy(context.getId());
        entity.setCreateTime(System.currentTimeMillis());
        return entity;
    }
}
