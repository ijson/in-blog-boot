package com.ijson.blog.dao.entity;

import com.ijson.mongo.support.model.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mongodb.morphia.annotations.*;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2020/1/15 11:54 AM
 */
@Getter
@Setter
@Entity(value = "PostDraft", noClassnameStored = true)
@ToString(callSuper = true)
@Indexes({
        @Index(name = "PD_TL",
                fields = {
                        @Field(value = PostDraftEntity.Fields.title),
                        @Field(value = PostDraftEntity.Fields.enable),
                        @Field(value = PostDraftEntity.Fields.createdBy),

                }),
        @Index(name = "PD_ENAME_SHAMID",
                fields = {
                        @Field(value = PostDraftEntity.Fields.ename),
                        @Field(value = PostDraftEntity.Fields.shamId)
                })
})
public class PostDraftEntity extends BaseEntity {

    @Id
    private String id;

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

    @Property(Fields.topicNames)
    private String topicNames;

    @Property(Fields.lastModifiedBy)
    private String lastModifiedBy;

    @Property(Fields.createdBy)
    private String createdBy;

    @Property(Fields.createTime)
    private long createTime;

    @Property(Fields.lastModifiedTime)
    private long lastModifiedTime;


    public interface Fields {
        String id = "_id";
        String userId = "userId";
        String content = "content";
        String topicNames = "topicNames";
        String createdBy = "createdBy";
        String createTime = "createTime";
        String enable = "enable";
        String deleted = "deleted";
        String lastModifiedBy = "lastModifiedBy";
        String title = "title";
        String lastModifiedTime = "lastModifiedTime";
        String ename = "ename";
        String shamId = "shamId";
    }


}
