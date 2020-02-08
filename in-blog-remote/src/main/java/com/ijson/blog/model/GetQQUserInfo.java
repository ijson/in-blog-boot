package com.ijson.blog.model;

import lombok.Data;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/8 9:26 PM
 */
public interface GetQQUserInfo {


    @Data
    class Result {
        private int ret;
        private String msg;
        private int is_lost;
        private String nickname;
        private String gender;
        private int gender_type;
        private String province;
        private String city;
        private String year;
        private String constellation;
        private String figureurl;
        private String figureurl_1;
        private String figureurl_2;
        private String figureurl_qq_1;
        private String figureurl_qq_2;
        private String figureurl_qq;
        private String figureurl_type;
        private String is_yellow_vip;
        private String vip;
        private String yellow_vip_level;
        private String level;
        private String is_yellow_year_vip;

    }
}
