package com.ijson.blog.remote.model;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/8 7:57 PM
 */
public interface GetQQOpenToken {


    @Data
    class Result {

        private static Pattern p = Pattern.compile("\\((.*?)\\)");


        private String client_id;
        private String openid;

        //    callback( {"client_id":"101382679","openid":"EA5D1B3607DCD25F6BA91018934C43E0"} );
        public static String format(String json) {
            Matcher m = p.matcher(json);

            while (m.find()) {
                return m.group(1);
            }

            return json;
        }
    }

}
