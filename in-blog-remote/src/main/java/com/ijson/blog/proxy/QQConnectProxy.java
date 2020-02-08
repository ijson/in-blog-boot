package com.ijson.blog.proxy;

import com.google.common.collect.Maps;
import com.ijson.blog.model.GetQQOpenToken;
import com.ijson.blog.model.GetQQUserInfo;
import com.ijson.blog.resource.QQConnectResource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/8 9:22 PM
 */
@Data
public class QQConnectProxy {

    private QQConnectResource qqConnectResource;

    /**
     * @param accessToken
     * @return openId
     */
    public String getQQOpenToken(String accessToken) {
        Map<String, String> prams = Maps.newHashMap();
        prams.put("access_token", accessToken);
        GetQQOpenToken.Result result = qqConnectResource.getQQOpenToken(prams);
        return result.getOpenid();
    }


    public GetQQUserInfo.Result getQQUserInfo(String accessToken, String appId, String openId) {
        Map<String, String> prams = Maps.newHashMap();
        prams.put("access_token", accessToken);
        prams.put("oauth_consumer_key", appId);
        prams.put("openid", openId);
        return qqConnectResource.getQQUserInfo(prams);
    }
}
