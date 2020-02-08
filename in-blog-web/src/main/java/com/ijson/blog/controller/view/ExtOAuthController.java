package com.ijson.blog.controller.view;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/2/8 11:35 AM
 */

import com.google.common.base.Strings;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.ConfigEntity;
import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.ExtLoginException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.blog.model.GetQQUserInfo;
import com.ijson.blog.proxy.QQConnectProxy;
import com.ijson.blog.util.EhcacheUtil;
import com.ijson.blog.util.PassportHelper;
import com.ijson.mongo.generator.util.ObjectId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Slf4j
@Controller()
@RequestMapping("/oauth")
public class ExtOAuthController extends BaseController {


    @Autowired
    private QQConnectProxy qqConnectProxy;

    @RequestMapping(value = "/callback/qq")
    public void login(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        //expires_in=7776000
        String accessToken = request.getParameter("access_token");
        String expires_in = request.getParameter("expires_in");

        if (Strings.isNullOrEmpty(accessToken)) {
            throw new ExtLoginException(BlogBusinessExceptionCode.ACCOUNT_LOGIN_ERROR);
        }

        ConfigEntity config = getConfig();

        String openId = qqConnectProxy.getQQOpenToken(accessToken);
        if (Strings.isNullOrEmpty(openId)) {
            throw new ExtLoginException(BlogBusinessExceptionCode.ACCOUNT_LOGIN_ERROR);
        }

        UserEntity userEntity = userService.findByQQOpenId(openId);
        if (Objects.isNull(userEntity)) {
            GetQQUserInfo.Result qqUserInfo = qqConnectProxy.getQQUserInfo(accessToken, config.getAppId(), openId);
            // 获取用户详细信息
            if (Objects.isNull(qqUserInfo)) {
                throw new ExtLoginException(BlogBusinessExceptionCode.ACCOUNT_LOGIN_ERROR);
            }

            //如果有用户信息,创建user
            userEntity = userService.createExtQQUser(qqUserInfo, accessToken, openId, config.getRegRoleId());
        }

        AuthContext authContext = userService.loginQQ(userEntity);

        String tokenId = ObjectId.getId();

        if (Objects.nonNull(authContext)) {
            EhcacheUtil.getInstance().put(Constant.loginUserCacheKey, tokenId, authContext);
            session.setAttribute("authContext", authContext);
            Cookie cookie = new Cookie(PassportHelper.getInstance().getCookieName(), tokenId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 30);
            response.addCookie(cookie);
        }

    }
}
