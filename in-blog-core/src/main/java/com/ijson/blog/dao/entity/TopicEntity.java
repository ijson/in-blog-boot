package com.ijson.blog.dao.entity;

import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.blog.model.SystemInfo;
import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;
import lombok.ToString;
import org.mongodb.morphia.annotations.*;

@Data
@Entity(value = "Topic", noClassnameStored = true)
@ToString(callSuper = true)
@Indexes({
        @Index(name = "T_TICN",
                fields = {
                        @Field(value = TopicEntity.Fields.topicName)
                }),

        @Index(name = "T_ENAME_SHAMID",
                fields = {
                        @Field(value = TopicEntity.Fields.ename),
                        @Field(value = TopicEntity.Fields.shamId)
                })
})
public class TopicEntity extends BaseEntity {


    @Id
    private String id;

    @Property(Fields.ename)
    private String ename;

    @Property(Fields.shamId)
    private String shamId;

    @Property(Fields.topicName)
    private String topicName;

    @Property(Fields.postCount)
    private long postCount;


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

    public static TopicEntity create(String topicName, AuthContext context) {
        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setDeleted(false);
        topicEntity.setEnable(true);
        topicEntity.setCreatedBy(context.getId());
        topicEntity.setCreateTime(System.currentTimeMillis());
        topicEntity.setLastModifiedBy(context.getId());
        topicEntity.setTopicName(topicName.toLowerCase().trim());
        topicEntity.setPostCount(1L);
        topicEntity.setEname(context.getEname());
        topicEntity.setLastModifiedTime(System.currentTimeMillis());
        return topicEntity;
    }


    public interface Fields {
        String id = "_id";
        String postCount = "postCount";
        String createdBy = "createdBy";
        String createTime = "createTime";
        String enable = "enable";
        String deleted = "deleted";
        String lastModifiedBy = "lastModifiedBy";
        String topicName = "topicName";
        String ename = "ename";
        String shamId = "shamId";
        String lastModifiedTime = "lastModifiedTime";

    }
}

