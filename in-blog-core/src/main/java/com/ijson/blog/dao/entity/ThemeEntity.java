package com.ijson.blog.dao.entity;

import com.ijson.blog.dao.model.ViewOrAdminType;
import lombok.Data;
import lombok.ToString;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/9 6:03 PM
 */
@Data
@Entity(value = "Theme", noClassnameStored = true)
@ToString(callSuper = true)
public class ThemeEntity {

    @Id
    private String id;

    @Property(Fields.cname)
    private String cname;

    @Property(Fields.ename)
    private String ename;

    @Property(Fields.type)
    private ViewOrAdminType type;

    @Property(Fields.desc)
    private String desc;


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
        String cname = "cname";
        String ename = "ename";
        String type = "type";
        String desc = "desc";

        String enable = "enable";
        String createdBy = "createdBy";
        String createTime = "createTime";
        String lastModifiedTime = "lastModifiedTime";
        String lastModifiedBy = "lastModifiedBy";
    }
}
