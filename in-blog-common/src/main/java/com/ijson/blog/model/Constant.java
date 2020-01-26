package com.ijson.blog.model;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/17 1:30 PM
 */
public interface Constant {

    String defaultTopicModel = "defaultTopicModel";
    String defaultTopicId = "defaultTopicId";
    String loginUserCacheKey = "loginUser";
    String remember = "remember";
    String UnknownUser = "未知用户";
    String UnknownRole = "未知角色";
    String SYSTEM = "system";

    enum ConfigType {
        site("网站设置"),
        switchType("开关设置"),
        fieldShow("博主字段信息显示");

        ConfigType(String desc) {
        }

    }

    enum MenuType {
        menu("菜单"),
        action("操作");

        MenuType(String desc) {

        }
    }
}
