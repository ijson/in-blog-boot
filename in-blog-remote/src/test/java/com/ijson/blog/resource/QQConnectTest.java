package com.ijson.blog.resource;

import com.google.common.collect.Maps;
import com.ijson.blog.BaseTest;
import com.ijson.blog.model.GetQQOpenToken;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/8 8:04 PM
 */
public class QQConnectTest extends BaseTest {

    @Autowired
    private QQConnectResource qqConnectResource;


    @Test
    public void getQQOpenToken() {
        Map<String, String> data = Maps.newHashMap();
        data.put("access_token", "148CD0D8BD6DFB3A3748D932F1D7E1D4");
        GetQQOpenToken.Result result = qqConnectResource.getQQOpenToken(data);
        System.out.println(1);
    }


    @Test
    public void getQQUserInfo() {
        Map<String, String> data = Maps.newHashMap();
        data.put("access_token", "148CD0D8BD6DFB3A3748D932F1D7E1D4");
        GetQQOpenToken.Result result = qqConnectResource.getQQOpenToken(data);
        data.put("oauth_consumer_key", "");
        data.put("openid", result.getOpenid());
        qqConnectResource.getQQUserInfo(data);
    }
}

