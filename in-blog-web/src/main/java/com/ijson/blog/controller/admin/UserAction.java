package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.controller.admin.model.BaseUserInfo;
import com.ijson.blog.controller.admin.model.Contact;
import com.ijson.blog.controller.admin.model.UpdPassword;
import com.ijson.blog.controller.admin.model.WebSet;
import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.ReplyCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.util.VerifyCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/15 5:29 AM
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserAction extends BaseController {

    private String updPwdCodeKey = "updPwdCodeKey";
    private String updPwdCodeTime = "updPwdCodeTime";


    @PostMapping(value = "/edit/webset")
    public Result editWebset(HttpServletRequest request, @RequestBody WebSet webSet) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("用户编辑用户信息时,未获取到当前登入人用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        UserEntity entity = userService.findUserById(webSet.getId());
        entity.setIndexName(webSet.getIndexName());
        userService.edit(entity);
        return Result.ok("更新成功!");
    }

    @PostMapping(value = "/edit/base")
    public Result editBase(HttpServletRequest request, @RequestBody BaseUserInfo baseUserInfo) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("用户编辑用户信息时,未获取到当前登入人用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        UserEntity entity = userService.findUserById(baseUserInfo.getId());


        entity.setCname(baseUserInfo.getCname());
        entity.setMobile(baseUserInfo.getMobile());
        entity.setEmail(baseUserInfo.getEmail());
        entity.setUniversityName(baseUserInfo.getUniversityName());
        entity.setUniversityLink(baseUserInfo.getUniversityLink());
        entity.setProfessional(baseUserInfo.getProfessional());
        entity.setStartTime(baseUserInfo.getStartTime());
        entity.setEndTime(baseUserInfo.getEndTime());

        userService.edit(entity);
        return Result.ok("更新成功!");
    }


    @PostMapping(value = "/edit/contact")
    public Result editContact(HttpServletRequest request, @RequestBody Contact contact) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("用户编辑用户信息时,未获取到当前登入人用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        UserEntity entity = userService.findUserById(contact.getId());


        entity.setWechat(contact.getWechat());
        entity.setWechatLink(contact.getWechatLink());
        entity.setWeibo(contact.getWeibo());
        entity.setWeiboLink(contact.getWeiboLink());
        entity.setQq(contact.getQq());
        entity.setTwitterName(contact.getTwitterName());
        entity.setTwitterLink(contact.getTwitterLink());
        entity.setFacebookName(contact.getFacebookName());
        entity.setFacebookLink(contact.getFacebookLink());

        userService.edit(entity);
        return Result.ok("更新成功!");
    }


    @PostMapping(value = "/edit/password")
    public Result editPassword(HttpServletRequest request, HttpSession session, @RequestBody UpdPassword updPassword) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("用户编辑用户信息时,未获取到当前登入人用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        String verCode = (String) session.getAttribute(updPwdCodeKey);
        Result result = VerifyCodeUtils.validImage(updPassword.getPwdVerCode(), verCode, request, session, updPwdCodeKey, updPwdCodeTime);
        if (result.getCode() != 0) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.CAPTCHA_ERROR_OR_NOT_PRESENT, result.getMessage());
        }

        if (Strings.isNullOrEmpty(updPassword.getOldPwd())) {
            return Result.error("旧密码不能为空!");
        }
        if (Strings.isNullOrEmpty(updPassword.getNewPwd())) {
            return Result.error("新密码不能为空!");
        }

        if (!updPassword.getNewPwd().equals(updPassword.getAgainPwd())) {
            return Result.error("新密码输入不一致!");
        }

        UserEntity entity = userService.findUserById(updPassword.getId());

        if (!entity.getPassword().equals(updPassword.getOldPwd())) {
            return Result.error("原始密码不正确!");
        }

        entity.setPassword(updPassword.getNewPwd());
        userService.edit(entity);
        return Result.ok("更新密码成功!");
    }


    @RequestMapping(value = "/edit/image", method = RequestMethod.GET)
    public void authImage(HttpServletResponse response, HttpSession session) throws IOException {
        generateVerification(response, session, updPwdCodeKey, updPwdCodeTime);
    }


}
