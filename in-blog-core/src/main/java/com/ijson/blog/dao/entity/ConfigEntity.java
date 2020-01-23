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

    @Property(Fields.openWechatLogin)
    private boolean openWechatLogin;

    @Property(Fields.openWeiboLogin)
    private boolean openWeiboLogin;

    @Property(Fields.openQQLogin)
    private boolean openQQLogin;

    @Property(Fields.showAdminFields)
    private List<String> showAdminFields;


    public interface Fields {
        String id = "_id";
        String type = "type";
        String siteName = "siteName";

        String openReg = "openReg";
        String openCmt = "openCmt";
        String showAdminInfo = "showAdminInfo";
        String openWechatLogin = "openWechatLogin";
        String openWeiboLogin = "openWeiboLogin";
        String openQQLogin = "openQQLogin";


        String showAdminFields = "showAdminFields";
    }
}
