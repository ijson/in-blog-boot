package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.controller.admin.model.UpdPassword;
import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.dao.query.UserQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.ReplyCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.UserInfo;
import com.ijson.blog.service.model.dtable.UserDTable;
import com.ijson.blog.util.VerifyCodeUtils;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
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


//    @PostMapping(value = "/edit/webset")
//    public Result editWebset(HttpServletRequest request, @RequestBody WebSet webSet) {
//        AuthContext context = getContext(request);
//        if (Objects.isNull(context)) {
//            log.info("用户编辑用户信息时,未获取到当前登入人用户信息");
//            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
//        }
//        UserEntity entity = userService.findUserById(webSet.getId());
//        entity.setIndexName(webSet.getIndexName());
//        userService.edit(entity);
//        return Result.ok("更新成功!");
//    }

//    @PostMapping(value = "/edit/base")
//    public Result editBase(HttpServletRequest request, @RequestBody BaseUserInfo baseUserInfo) {
//        AuthContext context = getContext(request);
//        if (Objects.isNull(context)) {
//            log.info("用户编辑用户信息时,未获取到当前登入人用户信息");
//            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
//        }
//        UserEntity entity = userService.findUserById(baseUserInfo.getId());
//
//
//        entity.setCname(baseUserInfo.getCname());
//        entity.setMobile(baseUserInfo.getMobile());
//        entity.setEmail(baseUserInfo.getEmail());
//        entity.setUniversityName(baseUserInfo.getUniversityName());
//        entity.setUniversityLink(baseUserInfo.getUniversityLink());
//        entity.setProfessional(baseUserInfo.getProfessional());
//        entity.setStartTime(baseUserInfo.getStartTime());
//        entity.setEndTime(baseUserInfo.getEndTime());
//
//        userService.edit(entity);
//        return Result.ok("更新成功!");
//    }

    @PostMapping(value = "/edit/user")
    public Result editBase(HttpServletRequest request, @RequestBody UserEntity myUser) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("用户编辑用户信息时,未获取到当前登入人用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        UserEntity entity = userService.findUserById(myUser.getId());

        entity.setCname(myUser.getCname());
        entity.setMobile(myUser.getMobile());
        entity.setEmail(myUser.getEmail());
        entity.setSchool(myUser.getSchool());
        entity.setSchoolLink(myUser.getSchoolLink());
        entity.setProfession(myUser.getProfession());
        entity.setBeginJobTime(myUser.getBeginJobTime());
        entity.setEndJobTime(myUser.getEndJobTime());
        entity.setWechat(myUser.getWechat());
        //entity.setWechatLink(myUser.getWechatLink());
        entity.setWeibo(myUser.getWeibo());
        //entity.setWeiboLink(contact.getWeiboLink());
        entity.setQq(myUser.getQq());
        entity.setTwitter(myUser.getTwitter());
        //entity.setTwitterLink(myUser.getTwitterLink());
        entity.setFacebook(myUser.getFacebook());
        //entity.setFacebookLink(myUser.getFacebookLink());
        userService.edit(entity);
        return Result.ok("更新成功!");
    }


//    @PostMapping(value = "/edit/contact")
//    public Result editContact(HttpServletRequest request, @RequestBody Contact contact) {
//        AuthContext context = getContext(request);
//        if (Objects.isNull(context)) {
//            log.info("用户编辑用户信息时,未获取到当前登入人用户信息");
//            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
//        }
//        UserEntity entity = userService.findUserById(contact.getId());
//
//
//        entity.setWechat(contact.getWechat());
//        entity.setWechatLink(contact.getWechatLink());
//        entity.setWeibo(contact.getWeibo());
//        entity.setWeiboLink(contact.getWeiboLink());
//        entity.setQq(contact.getQq());
//        entity.setTwitterName(contact.getTwitterName());
//        entity.setTwitterLink(contact.getTwitterLink());
//        entity.setFacebookName(contact.getFacebookName());
//        entity.setFacebookLink(contact.getFacebookLink());
//
//        userService.edit(entity);
//        return Result.ok("更新成功!");
//    }


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

        if (Strings.isNullOrEmpty(updPassword.getOldPassword())) {
            return Result.error("旧密码不能为空!");
        }
        if (Strings.isNullOrEmpty(updPassword.getNewPassword())) {
            return Result.error("新密码不能为空!");
        }

        if (!updPassword.getNewPassword().equals(updPassword.getAgainPassword())) {
            return Result.error("新密码输入不一致!");
        }

        UserEntity entity = userService.findUserById(updPassword.getId());

        if (!entity.getPassword().equals(updPassword.getOldPassword())) {
            return Result.error("原始密码不正确!");
        }

        entity.setPassword(updPassword.getNewPassword());
        userService.edit(entity);
        return Result.ok("更新密码成功!");
    }


    @RequestMapping(value = "/edit/image", method = RequestMethod.GET)
    public void authImage(HttpServletResponse response, HttpSession session) throws IOException {
        generateVerification(response, session, updPwdCodeKey, updPwdCodeTime);
    }


    @RequestMapping("/list")
    @ResponseBody
    public UserDTable list(Integer start, Integer length, HttpServletRequest request) {

        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            return UserDTable.create(Lists.newArrayList(), null, start);
        }


        String keyWord = request.getParameter("search[value]");

        Page page = new Page();
        if (Objects.nonNull(start)) {
            page.setPageNumber((start / length) + 1);
        }
        if (Objects.nonNull(length)) {
            page.setPageSize(length);
        }


        UserQuery query = new UserQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setCname(keyWord);
        }

        PageResult<UserEntity> result = userService.find(query, page);

        if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getDataList())) {
            return new UserDTable();
        }

        List<UserEntity> dataList = result.getDataList();
        List<UserInfo> userInfos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            userInfos = UserInfo.creaetUserList(dataList);
        }

        return UserDTable.create(userInfos, result.getTotal(), start);
    }
}
