package com.ijson.blog.dao.entity;

import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.mongodb.morphia.annotations.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(value = "User", noClassnameStored = true)
@ToString(callSuper = true)
@Indexes({
        @Index(name = "U_MEMA",
                fields = {
                        @Field(value = UserEntity.Fields.ename),
                        @Field(value = UserEntity.Fields.mobile),
                        @Field(value = UserEntity.Fields.email),
                })
})
public class UserEntity extends BaseEntity {


    @Id
    private String id;

    @Property(Fields.ename)
    private String ename;


    @Property(Fields.cname)
    private String cname;

    @Property(Fields.roleId)
    private String roleId;

    @Property(Fields.password)
    private String password;

    @Property(Fields.avatar)
    private String avatar;

    @Property(Fields.email)
    private String email;

    @Property(Fields.wechat)
    private String wechat;

    @Property(Fields.wechatLink)
    private String wechatLink;

    @Property(Fields.weibo)
    private String weibo;

    @Property(Fields.weiboLink)
    private String weiboLink;

    @Property(Fields.qq)
    private String qq;

    @Property(Fields.editorType)
    private String editorType;


    @Property(Fields.mobile)
    private String mobile;


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


    @Property(Fields.twitterName)
    private String twitterName;

    @Property(Fields.twitterLink)
    private String twitterLink;


    @Property(Fields.facebookName)
    private String facebookName;


    @Property(Fields.facebookLink)
    private String facebookLink;


    @Property(Fields.universityName)
    private String universityName;


    @Property(Fields.universityLink)
    private String universityLink;


    @Property(Fields.professional)
    private String professional;


    @Property(Fields.workStartTime)
    private Long workStartTime;


    @Property(Fields.workEndTime)
    private Long workEndTime;//-1 表示至今

    @Property(Fields.indexName)
    private String indexName;

    private volatile String startTime;
    private volatile String endTime;
    private volatile String roleCname;


    public static UserEntity create(String ename, String cname, String email, String mobile, String password, String qq, String wechat, String weibo) {
        UserEntity userEntity = new UserEntity();
        userEntity.setCreateTime(System.currentTimeMillis());
        userEntity.setDeleted(false);
        userEntity.setEmail(email);
        userEntity.setCname(cname);
        userEntity.setEnable(true);
        userEntity.setEname(ename);
        userEntity.setMobile(mobile);
        userEntity.setPassword(password);
        userEntity.setQq(qq);
        userEntity.setWechat(wechat);
        userEntity.setWeibo(weibo);
        userEntity.setLastModifiedTime(System.currentTimeMillis());
        return userEntity;
    }


    public interface Fields {
        String id = "_id";
        String ename = "ename";
        String password = "password";
        String email = "email";
        String cname = "cname";
        String wechat = "wechat";
        String weibo = "weibo";
        String qq = "qq";
        String mobile = "mobile";
        String editorType = "editorType";
        String createdBy = "createdBy";
        String createTime = "createTime";
        String enable = "enable";
        String deleted = "deleted";
        String lastModifiedBy = "lastModifiedBy";
        String lastModifiedTime = "lastModifiedTime";
        String twitterName = "twitterName";
        String twitterLink = "twitterLink";
        String facebookName = "facebookName";
        String facebookLink = "facebookLink";
        String universityName = "universityName";
        String universityLink = "universityLink";
        String professional = "professional";
        String workStartTime = "workStartTime";
        String workEndTime = "workEndTime";//-1 表示至今
        String indexName = "indexName";
        String wechatLink = "wechatLink";
        String weiboLink = "weiboLink";
        String avatar = "avatar";
        String roleId = "roleId";
    }

    public static UserEntity unknownUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setCname("未知用户");
        return userEntity;
    }

}

