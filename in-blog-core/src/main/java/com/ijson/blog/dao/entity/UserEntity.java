package com.ijson.blog.dao.entity;

import com.ijson.blog.model.Constant;
import com.ijson.blog.model.RegSourceType;
import com.ijson.mongo.support.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.mongodb.morphia.annotations.*;

import java.util.Map;

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

    @Property(Fields.weibo)
    private String weibo;

    @Property(Fields.qq)
    private String qq;

    @Property(Fields.editorType)
    private String editorType;


    @Property(Fields.mobile)
    private String mobile;

    /**
     * 性别
     */
    @Property(Fields.gender)
    private String gender;


    @Property(Fields.lastModifiedBy)
    private String lastModifiedBy;

    @Property(Fields.lastModifiedTime)
    private long lastModifiedTime;

    @Property(Fields.deleted)
    private boolean deleted;

    @Property(Fields.enable)
    private boolean enable;

    @Property(Fields.createdBy)
    private String createdBy;

    @Property(Fields.createTime)
    private long createTime;


    @Property(Fields.twitter)
    private String twitter;


    @Property(Fields.facebook)
    private String facebook;


    @Property(Fields.school)
    private String school;


    @Property(Fields.schoolLink)
    private String schoolLink;


    @Property(Fields.profession)
    private String profession;


    @Property(Fields.beginJobTime)
    private String beginJobTime;


    @Property(Fields.endJobTime)
    private String endJobTime;//-1 表示至今

    @Property(Fields.roleCname)
    private String roleCname;

    @Property(Fields.status)
    private Constant.UserStatus status;

    @Property(Fields.regSourceType)
    private RegSourceType regSourceType = RegSourceType.reg;

    @Property(Fields.qqAccessToken)
    private String qqAccessToken;

    /**
     * 默认3个月  需要自动续期
     */
    @Property(Fields.qqAccessTokenCreateTime)
    private Long qqAccessTokenCreateTime;
    /**
     * qq login user id
     */
    @Property(Fields.qqOpenId)
    private String qqOpenId;

    @Embedded
    private Map<String,Object> qqExtData;



    public static UserEntity create(String ename,
                                    String cname,
                                    String email,
                                    String mobile,
                                    String password,
                                    String qq,
                                    String wechat,
                                    String weibo,
                                    String roleId) {
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
        userEntity.setRoleId(roleId);
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
        String avatar = "avatar";
        String roleId = "roleId";
        String twitter = "twitter";
        String facebook = "facebook";
        String school = "school";
        String schoolLink = "schoolLink";
        String profession = "profession";
        String beginJobTime = "beginJobTime";
        String endJobTime = "endJobTime";
        String roleCname = "roleCname";
        String status = "status";
        String qqOpenId = "qqOpenId";
        String qqAccessToken = "qqAccessToken";
        String qqAccessTokenCreateTime = "qqAccessTokenCreateTime";
        String gender = "gender";
        String regSourceType = "regSourceType";
    }

}

