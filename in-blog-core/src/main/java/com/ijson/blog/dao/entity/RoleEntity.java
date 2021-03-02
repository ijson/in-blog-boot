package com.ijson.blog.dao.entity;

import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.List;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/13 8:02 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity(value = "Role", noClassnameStored = true)
@ToString(callSuper = true)
public class RoleEntity extends BaseEntity {

    @Id
    private String id;

    @Property(Fields.ename)
    private String ename;

    @Property(Fields.cname)
    private String cname;

    @Property(Fields.authIds)
    private List<String> authIds;

    @Property(Fields.userIds)
    private List<String> userIds;

    @Property(Fields.enable)
    private boolean enable;

    @Property(Fields.createdBy)
    private String createdBy;

    @Property(Fields.createTime)
    private long createTime;


    @Property(Fields.lastModifiedBy)
    private String lastModifiedBy;

    @Property(Fields.lastModifiedTime)
    private long lastModifiedTime;

    /**
     * 发表文章是否审核
     */
    @Property(Fields.verify)
    private Boolean verify;

    /**
     * 发表评论是否审核
     */
    @Property(Fields.verifyCmt)
    private Boolean verifyCmt;


    private volatile List<String> keys;


    public interface Fields {
        String id = "_id";
        String cname = "cname";
        String ename = "ename";
        String userIds = "userIds";
        String authIds = "authIds";
        String verify = "verify";
        String verifyCmt = "verifyCmt";

        String enable = "enable";
        String createdBy = "createdBy";
        String createTime = "createTime";
        String lastModifiedBy = "lastModifiedBy";
        String lastModifiedTime = "lastModifiedTime";

    }
}
