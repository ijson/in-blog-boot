package com.ijson.blog.model;

import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.ijson.rest.proxy.util.JsonUtil;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/9 2:40 PM
 */
public interface GetQQTokenByCode {

    @Data
    class Result {
        private String access_token;
        private Long expires_in;
        private String refresh_token;
        //access_token=148CD0D8BD6DFB3A2FF07DFC3B69376D&expires_in=7776000&refresh_token=BE70C41E7C87192939EC19A9F57241F0
        public static String format(String json) {
            List<String> data = Splitter.on("&").splitToList(json);
            Map<String, String> rst = Maps.newHashMap();
            for (String datum : data) {
                List<String> strings = Splitter.on("=").splitToList(datum);
                String key = strings.get(0);
                String value = "";
                if (strings.size() > 1) {
                    value = strings.get(1);
                }

                rst.put(key, value);
            }
            return JsonUtil.toJson(rst);
        }
    }
}
