package com.ijson.blog.dao.entity;

import com.ijson.blog.dao.model.ReplyType;
import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.mongodb.morphia.annotations.*;

import java.util.Objects;

@Data
@Entity(value = "Comment", noClassnameStored = true)
@ToString(callSuper = true)
@Indexes({
        @Index(name = "R_ENAME_SHAMID",
                fields = {
                        @Field(value = CommentEntity.Fields.ename),
                        @Field(value = CommentEntity.Fields.shamId)
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

    //评论还是回复
    @Property(Fields.replyType)
    private ReplyType replyType;

    @Property(Fields.userId)
    private String userId;

    @Property(Fields.praise)
    private Long praise;

    @Property(Fields.replyId)
    private String replyId;


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


    public Long getPraise() {
        return Objects.isNull(praise) ? 0 : praise;
    }

    public interface Fields {
        String id = "_id";
        String userId = "userId";
        String postId = "postId";
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
        String replyType = "replyType";
        String browse = "browse";
        String host = "host";
        String praise = "praise";
        String replyId = "replyId";
        String lastModifiedTime = "lastModifiedTime";
    }
}

