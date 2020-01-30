package com.ijson.blog.dao.entity;

import lombok.Data;
import lombok.ToString;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * desc: 展现在<head></head>中的数据
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/30 7:59 PM
 */
@Data
@Entity(value = "Header", noClassnameStored = true)
@ToString(callSuper = true)
public class HeaderEntity {


    @Id
    private String id;

    @Property(Fields.cname)
    private String cname;

    @Property(Fields.code)
    private String code;

    @Property(Fields.enable)
    private boolean enable;

    @Property(Fields.createdBy)
    private String createdBy;

    @Property(Fields.createTime)
    private long createTime;

    @Property(Fields.lastModifiedTime)
    private long lastModifiedTime;

    @Property(Fields.lastModifiedBy)
    private String lastModifiedBy;


    public interface Fields {
        String id = "_id";
        String enable = "enable";
        String createdBy = "createdBy";
        String createTime = "createTime";
        String lastModifiedTime = "lastModifiedTime";
        String lastModifiedBy = "lastModifiedBy";
        String cname = "cname";
        String code = "code";
    }
}
