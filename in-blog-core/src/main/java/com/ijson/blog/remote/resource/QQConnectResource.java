package com.ijson.blog.remote.resource;

import com.ijson.blog.remote.model.GetQQOpenToken;
import com.ijson.blog.remote.model.GetQQTokenByCode;
import com.ijson.blog.remote.model.GetQQUserInfo;
import com.ijson.rest.proxy.annotation.GET;
import com.ijson.rest.proxy.annotation.PathParams;
import com.ijson.rest.proxy.annotation.RestResource;

import java.util.Map;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/8 7:51 PM
 */
@RestResource(value = "QQConnect", desc = "QQ互联", codec = "com.ijson.blog.codec.QQConnectCodeC", contentType = "application/json")
public interface QQConnectResource {

    @GET(value = "/oauth2.0/me?access_token={access_token}", contentType = "application/json")
    GetQQOpenToken.Result getQQOpenToken(@PathParams Map<String, String> params);


    @GET(value = "/user/get_user_info?access_token={access_token}&oauth_consumer_key={oauth_consumer_key}&openid={openid}", contentType = "application/json")
    GetQQUserInfo.Result getQQUserInfo(@PathParams Map<String, String> params);


    @GET(value = "/oauth2.0/token?grant_type=authorization_code&client_id={client_id}&client_secret={client_secret}&code={code}&redirect_uri={redirect_uri}", contentType = "application/json")
    GetQQTokenByCode.Result getQQTokenByCode(@PathParams Map<String, String> params);
}
