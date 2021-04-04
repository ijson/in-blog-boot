package com.ijson.blog.dao.entity;

import com.ijson.blog.model.Constant;
import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.Objects;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 11:39 PM
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity(value = "Auth", noClassnameStored = true)
@ToString(callSuper = true)
public class AuthEntity extends BaseEntity {

    @Id
    private String id;

    @Property(Fields.ename)
    private String ename;

    @Property(Fields.fatherId)
    private String fatherId;

    @Property(Fields.cname)
    private String cname;

    @Property(Fields.path)
    private String path;

    @Property(Fields.menuType)
    private Constant.MenuType menuType;

    @Property(Fields.order)
    private Integer order;

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

    @Property(Fields.showMenu)
    private Boolean showMenu;

    public Boolean getShowMenu() {
        if (Objects.isNull(showMenu)) {
            return false;
        }
        return showMenu;
    }

    public interface Fields {
        String id = "_id";
        String cname = "cname";
        String ename = "ename";
        String path = "path";
        String menuType = "menuType";
        String order = "order";
        String enable = "enable";
        String fatherId = "fatherId";
        String createdBy = "createdBy";
        String createTime = "createTime";
        String lastModifiedBy = "lastModifiedBy";
        String lastModifiedTime = "lastModifiedTime";
        String showMenu = "showMenu";

    }
}
