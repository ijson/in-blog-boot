package com.ijson.blog.dao.entity;

import com.ijson.blog.dao.model.ReplyType;
import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.mongodb.morphia.annotations.*;

@Data
@Entity(value = "Comment", noClassnameStored = true)
@ToString(callSuper = true)
@Indexes({
        @Index(name = "R_ENAME_SHAMID",
                fields = {
                        @Field(value = ReplyEntity.Fields.ename),
                        @Field(value = ReplyEntity.Fields.shamId)
                })
})
public class CommentEntity extends BaseEntity {


    @Id
    private String id;
    @Property(Fields.ename)
    private String ename;

    @Property(Fields.shamId)
    private String shamId;

    @Property(Fields.postId)
    private String postId;

    @Property(Fields.content)
    private String content;

    //被评论人头像
    @Property(Fields.toAvatar)
    private String toAvatar;
    //被评论人名称
    @Property(Fields.toCname)
    private String toCname;
    //被评论人userId
    @Property(Fields.toUserId)
    private String toUserId;


    //评论人头像
    @Property(Fields.fromAvatar)
    private String fromAvatar;
    //评论人名称
    @Property(Fields.fromCname)
    private String fromCname;
    //评论人userId
    @Property(Fields.fromUserId)
    private String fromUserId;

    //评论还是回复
    @Property(Fields.replyType)
    private ReplyType replyType;

    @Property(Fields.fatherId)
    private String fatherId;

    //浏览器
    @Property(Fields.browse)
    private String browse;

    @Property(Fields.os)
    private String os;

    @Property(Fields.userAgent)
    private String userAgent;

    @Property(Fields.host)
    private String host;

    @Property(Fields.lastModifiedBy)
    private String lastModifiedBy;

    @Property(Fields.lastModifiedTime)
    private long lastModifiedTime;

    @Property(Fields.deleted)
    private Boolean deleted;

    @Property(Fields.enable)
    private boolean enable;

    @Property(Fields.createdBy)
    private String createdBy;

    @Property(Fields.createTime)
    private long createTime;

    @Property(Fields.author)
    private String author;


    public interface Fields {
        String id = "_id";
        String topicId = "topicId";
        String userId = "userId";
        String content = "content";
        String createdBy = "createdBy";
        String createTime = "createTime";
        String enable = "enable";
        String deleted = "deleted";
        String lastModifiedBy = "lastModifiedBy";
        String userAgent = "userAgent";
        String os = "os";
        String shamId = "shamId";
        String ename = "ename";
        String toAvatar = "toAvatar";
        String toCname = "toCname";
        String toUserId = "toUserId";
        String fromAvatar = "fromAvatar";
        String fromCname = "fromCname";
        String fromUserId = "fromUserId";
        String replyType = "replyType";
        String browse = "browse";
        String host = "host";
        String fatherId = "fatherId";
        String lastModifiedTime = "lastModifiedTime";
        String postId = "postId";
        String author = "author";
    }
}

