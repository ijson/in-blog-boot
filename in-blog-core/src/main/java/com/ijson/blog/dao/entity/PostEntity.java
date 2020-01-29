package com.ijson.blog.dao.entity;

import com.google.common.collect.Lists;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.mongo.support.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity(value = "Post", noClassnameStored = true)
@ToString(callSuper = true)
@Indexes({
        @Index(name = "P_TL",
                fields = {
                        @Field(value = PostEntity.Fields.title),
                        @Field(value = PostEntity.Fields.enable),
                        @Field(value = PostEntity.Fields.createdBy),

                }),
        @Index(name = "P_TPCID",
                fields = {
                        @Field(value = PostEntity.Fields.topicId)
                }),
        @Index(name = "P_ENAME_SHAMID",
                fields = {
                        @Field(value = PostEntity.Fields.ename),
                        @Field(value = PostEntity.Fields.shamId)
                })
})
public class PostEntity extends BaseEntity {


    @Id
    private String id;

    @Property(Fields.topicId)
    private List<String> topicId;

    @Property(Fields.userId)
    private String userId;

    @Property(Fields.ename)
    private String ename;

    @Property(Fields.shamId)
    private String shamId;

    @Property(Fields.content)
    private String content;

    @Property(Fields.title)
    private String title;

    @Property(Fields.pros)
    private long pros;

    @Property(Fields.cons)
    private long cons;


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

    @Property(Fields.lastModifiedTime)
    private long lastModifiedTime;

    @Property(Fields.draftId)
    private String draftId;

    @Property(Fields.status)
    private Constant.PostStatus status;

    @Property(Fields.reason)
    private String reason;

    @Property(Fields.trigger)
    private Constant.AuditTrigger trigger;

    private long views;

    private long reply;

    private List<TopicEntity> topicName;

    private volatile String cname;

    private volatile boolean isCreate;

    public interface Fields {
        String id = "_id";
        String topicId = "topicId";
        String userId = "userId";
        String content = "content";
        String pros = "pros";
        String cons = "cons";
        String createdBy = "createdBy";
        String createTime = "createTime";
        String enable = "enable";
        String deleted = "deleted";
        String lastModifiedBy = "lastModifiedBy";
        String title = "title";
        String lastModifiedTime = "lastModifiedTime";
        String ename = "ename";
        String shamId = "shamId";
        String draftId = "draftId";
        String status = "status";
        String reason = "reason";
        String trigger = "trigger";
    }


    public static PostEntity create(String id, String userId, String title, String content, List<TopicEntity> topicEntity,String ename) {
        PostEntity entity = new PostEntity();
        entity.setEname(ename);
        entity.setId(id);
        entity.setUserId(userId);
        entity.setTitle(title);
        entity.setContent(content);
        entity.setCreatedBy(userId);
        entity.setCreateTime(System.currentTimeMillis());
        entity.setLastModifiedBy(userId);
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setDeleted(false);
        entity.setEnable(true);
        if (CollectionUtils.isNotEmpty(topicEntity)) {
            entity.setTopicId(topicEntity.stream().map(TopicEntity::getId).collect(Collectors.toList()));
        } else {
            entity.setTopicId(Lists.newArrayList(Constant.defaultTopicId));
        }

        return entity;
    }


    public static PostEntity update(AuthContext context, String id, String title, String content, List<TopicEntity> topicEntity, List<String> existIds) {
        PostEntity entity = new PostEntity();
        entity.setTitle(title);
        entity.setContent(content);
        entity.setId(id);
        entity.setLastModifiedBy(context.getId());
        if (CollectionUtils.isNotEmpty(topicEntity)) {
            List<String> topicIds = topicEntity.stream().map(TopicEntity::getId).collect(Collectors.toList());
            entity.setTopicId(topicIds);
        } else {
            entity.setTopicId(Lists.newArrayList(Constant.defaultTopicId));
        }
        return entity;
    }
}

