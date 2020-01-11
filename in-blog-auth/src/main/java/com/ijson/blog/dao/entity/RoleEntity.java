package com.ijson.blog.dao.entity;

import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Permission;
import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.mongodb.morphia.annotations.*;

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
@Indexes({
        @Index(name = "N_T_P_S",
                fields = {
                        @Field(value = RoleEntity.Fields.ename),
                        @Field(value = RoleEntity.Fields.parentRoleId),
                        @Field(value = RoleEntity.Fields.status)
                })
})
public class RoleEntity extends BaseEntity {

    @Id
    private String id;

    @Property(Fields.ename)
    private String ename;

    @Property(Fields.userIds)
    private List<String> userIds;

    @Property(Fields.cname)
    private String cname;

    @Property(Fields.parentRoleId)
    private String parentRoleId;

    @Property(Fields.status)
    private String status;

    @Embedded
    private List<Permission> permission;

    @Property(Fields.remark)
    private String remark;

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


    public interface Fields {
        String id = "_id";
        String cname = "cname";
        String ename = "ename";
        String parentRoleId = "parentRoleId";
        String status = "status";
        String remark = "remark";
        String userIds = "userIds";

        String lastModifiedBy = "lastModifiedBy";
        String lastModifiedTime = "lastModifiedTime";
        String deleted = "deleted";
        String enable = "enable";
        String createdBy = "createdBy";
        String createTime = "createTime";
        String permission = "permission";
    }


    public static RoleEntity create(AuthContext authContext,
                                    String cname,
                                    String ename,
                                    String remark,
                                    String status,
                                    List<Permission> permission,
                                    List<String> userIds) {
        RoleEntity entity = new RoleEntity();
        entity.setCname(cname);
        entity.setEname(ename);
        entity.setCreatedBy(authContext.getId());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setDeleted(false);
        entity.setEnable(true);
        entity.setLastModifiedBy(authContext.getId());
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setParentRoleId("0");
        entity.setRemark(remark);
        entity.setStatus(status);
        entity.setPermission(permission);
        entity.setUserIds(userIds);
        return entity;
    }
}
