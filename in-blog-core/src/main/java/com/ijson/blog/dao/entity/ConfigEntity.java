package com.ijson.blog.dao.entity;

import com.ijson.blog.model.Constant;
import lombok.Data;
import lombok.ToString;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import java.util.List;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/23 12:17 AM
 */
@Data
@Entity(value = "Config", noClassnameStored = true)
@ToString(callSuper = true)
public class ConfigEntity {

    @Id
    private String id;

    @Property(Fields.type)
    private Constant.ConfigType type;

    @Property(Fields.siteName)
    private String siteName;

    @Property(Fields.openReg)
    private boolean openReg;

    @Property(Fields.openCmt)
    private boolean openCmt;

    @Property(Fields.showAdminInfo)
    private boolean showAdminInfo;

    @Property(Fields.dynamicMenu)
    private boolean dynamicMenu;

    @Property(Fields.openWechatLogin)
    private boolean openWechatLogin;

    @Property(Fields.openWeiboLogin)
    private boolean openWeiboLogin;

    @Property(Fields.openQQLogin)
    private boolean openQQLogin;

    @Property(Fields.showAdminFields)
    private List<String> showAdminFields;

    @Property(Fields.regRoleId)
    private String regRoleId;

//    @Property(Fields.ename)
//    private String ename;
//    @Property(Fields.cname)
//    private String cname;
//    @Property(Fields.mobile)
//    private String mobile;
//    @Property(Fields.email)
//    private String email;
//    @Property(Fields.school)
//    private String school;
//    @Property(Fields.schoolUrl)
//    private String schoolUrl;
//    @Property(Fields.profession)
//    private String profession;
//    @Property(Fields.beginJobTime)
//    private String beginJobTime;
//    @Property(Fields.endJobTime)
//    private String endJobTime;
//    @Property(Fields.wechat)
//    private String wechat;
//    @Property(Fields.webo)
//    private String webo;
//    @Property(Fields.qq)
//    private String qq;
//    @Property(Fields.twitter)
//    private String twitter;
//    @Property(Fields.facebook)
//    private String facebook;


    public interface Fields {
        String id = "_id";
        String type = "type";
        String siteName = "siteName";
        String regRoleId = "regRoleId";

        String openReg = "openReg";
        String openCmt = "openCmt";
        String dynamicMenu = "dynamicMenu";
        String showAdminInfo = "showAdminInfo";
        String openWechatLogin = "openWechatLogin";
        String openWeiboLogin = "openWeiboLogin";
        String openQQLogin = "openQQLogin";


        String showAdminFields = "showAdminFields";


//        String ename = "ename";
//        String cname = "cname";
//        String mobile = "mobile";
//        String email = "email";
//        String school = "school";
//        String schoolUrl = "schoolUrl";
//        String profession = "profession";
//        String beginJobTime = "beginJobTime";
//        String endJobTime = "endJobTime";
//        String wechat = "wechat";
//        String webo = "webo";
//        String qq = "qq";
//        String twitter = "twitter";
//        String facebook = "facebook";
    }
}
