package com.ijson.blog.dao.entity;

import lombok.Data;
import lombok.ToString;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/30 7:59 PM
 */
@Data
@Entity(value = "IndexMenu", noClassnameStored = true)
@ToString(callSuper = true)
public class IndexMenuEntity {


    @Id
    private String id;

    @Property(Fields.cname)
    private String cname;

    @Property(Fields.ename)
    private String ename;

    @Property(Fields.path)
    private String path;

    @Property(Fields.order)
    private Integer order;

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
        String path = "path";
        String ename = "ename";
        String order = "order";
    }
}
