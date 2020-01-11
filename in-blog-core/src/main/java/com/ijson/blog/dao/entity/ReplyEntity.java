package com.ijson.blog.dao.entity;

import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.mongodb.morphia.annotations.*;

@Data
@Entity(value = "Reply", noClassnameStored = true)
@ToString(callSuper = true)
@Indexes({
        @Index(name = "R_PID",
                fields = {
                        @Field(value = ReplyEntity.Fields.postId),
                }),
        @Index(name = "R_ENAME_SHAMID",
                fields = {
                        @Field(value = ReplyEntity.Fields.ename),
                        @Field(value = ReplyEntity.Fields.shamId)
                })
})
public class ReplyEntity extends BaseEntity {


    @Id
    private String id;

    @Property(Fields.topicId)
    private String topicId;

    @Property(Fields.userId)
    private String userId;

    @Property(Fields.postId)
    private String postId;

    @Property(Fields.ename)
    private String ename;

    @Property(Fields.shamId)
    private String shamId;

    @Property(Fields.content)
    private String content;

    @Property(Fields.portraits)
    private String portraits;

    @Property(Fields.replyName)
    private String replyName;

    @Property(Fields.beReplyName)
    private String beReplyName;

    @Property(Fields.address)
    private String address;

    @Property(Fields.ipAddress)
    private String ipAddress;

    @Property(Fields.userAgent)
    private String userAgent;

    @Property(Fields.browse)
    private String browse;

    @Property(Fields.os)
    private String os;

    @Property(Fields.fatherId)
    private String fatherId;

    @Property(Fields.lastModifiedBy)
    private String lastModifiedBy;

    @Property(Fields.deleted)
    private Boolean deleted;

    @Property(Fields.enable)
    private boolean enable;

    @Property(Fields.createdBy)
    private String createdBy;

    @Property(Fields.createTime)
    private long createTime;

    @Property(Fields.avatar)
    private String avatar;


    public interface Fields {
        String id = "_id";
        String topicId = "topicId";
        String userId = "userId";
        String postId = "postId";
        String content = "content";
        String createdBy = "createdBy";
        String createTime = "createTime";
        String enable = "enable";
        String deleted = "deleted";
        String lastModifiedBy = "lastModifiedBy";
        String portraits = "portraits";
        String replyName = "replyName";
        String beReplyName = "beReplyName";
        String address = "address";
        String browse = "browse";
        String ipAddress = "ipAddress";
        String fatherId = "fatherId";
        String userAgent = "userAgent";
        String os = "os";
        String avatar = "avatar";
        String shamId = "shamId";
        String ename = "ename";
    }
}

