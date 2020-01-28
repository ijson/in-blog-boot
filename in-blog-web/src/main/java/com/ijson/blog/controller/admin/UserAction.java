package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.ConfigEntity;
import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.dao.query.UserQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.ReplyCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.V2Result;
import com.ijson.blog.service.model.info.UpdPasswordInfo;
import com.ijson.blog.service.model.info.UserInfo;
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

    @PostMapping(value = "/addup")
    public Result editBase(HttpServletRequest request, @RequestBody UserEntity myUser) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);

        if (Strings.isNullOrEmpty(myUser.getEname())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_ENAME_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(myUser.getCname())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_CNAME_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(myUser.getEmail())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_EMAIL_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(myUser.getRoleId())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.ROLE_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(myUser.getId())) {
            return createUser(myUser);
        }

        UserEntity entity = userService.findInternalById(myUser.getId());

        entity.setPassword(myUser.getPassword());
        entity.setCname(myUser.getCname());
        entity.setMobile(myUser.getMobile());
        entity.setEmail(myUser.getEmail());
        entity.setSchool(myUser.getSchool());
        entity.setSchoolLink(myUser.getSchoolLink());
        entity.setProfession(myUser.getProfession());
        entity.setBeginJobTime(myUser.getBeginJobTime());
        entity.setEndJobTime(myUser.getEndJobTime());
        entity.setWechat(myUser.getWechat());
        entity.setWeibo(myUser.getWeibo());
        entity.setWeibo(myUser.getWeibo());
        entity.setQq(myUser.getQq());
        entity.setTwitter(myUser.getTwitter());
        entity.setFacebook(myUser.getFacebook());
        entity.setRoleId(myUser.getRoleId());

        userService.edit(entity);
        return Result.ok("更新成功!");
    }

    private Result createUser(UserEntity myUser) {
        UserEntity entity = new UserEntity();
        entity.setPassword(myUser.getPassword());
        entity.setEname(myUser.getEname());
        entity.setCname(myUser.getCname());
        entity.setMobile(myUser.getMobile());
        entity.setEmail(myUser.getEmail());
        entity.setSchool(myUser.getSchool());
        entity.setSchoolLink(myUser.getSchoolLink());
        entity.setProfession(myUser.getProfession());
        entity.setBeginJobTime(myUser.getBeginJobTime());
        entity.setEndJobTime(myUser.getEndJobTime());
        entity.setWechat(myUser.getWechat());
        entity.setWeibo(myUser.getWeibo());
        entity.setWeibo(myUser.getWeibo());
        entity.setQq(myUser.getQq());
        entity.setTwitter(myUser.getTwitter());
        entity.setFacebook(myUser.getFacebook());

        if (Strings.isNullOrEmpty(myUser.getRoleId())) {
            ConfigEntity allConfig = webSiteService.findAllConfig();
            entity.setRoleId(allConfig.getRegRoleId());
        } else {
            entity.setRoleId(myUser.getRoleId());
        }
        userService.reg(entity);
        return Result.ok("注册成功!");
    }


    @PostMapping(value = "/enable/{id}")
    public Result enable(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        UserEntity entity = userService.findInternalById(id);

        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }

        if (webEname.equals(entity.getEname())) {
            log.info("系统管理员不允许禁用");
            throw new ReplyCreateException(BlogBusinessExceptionCode.ADMINISTRATOR_ACCOUNTS_ARE_NOT_ALLOWED_TO_BE_DISABLED_OR_DELETED);
        }

        userService.enable(id, !entity.isEnable(), context.getId());
        return Result.ok(entity.isEnable() ? "禁用成功!" : "启用成功!");
    }


    @PostMapping(value = "/delete/{id}")
    public Result delete(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        UserEntity entity = userService.findInternalById(id);

        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }

        if (webEname.equals(entity.getEname())) {
            log.info("系统管理员不允许删除");
            throw new ReplyCreateException(BlogBusinessExceptionCode.ADMINISTRATOR_ACCOUNTS_ARE_NOT_ALLOWED_TO_BE_DISABLED_OR_DELETED);
        }

        if (entity.isEnable()) {
            log.info("账号启用状态无法删除");
            throw new ReplyCreateException(BlogBusinessExceptionCode.ENABLED_STATE_CANNOT_BE_DELETED);
        }

        userService.delete(id, !entity.isDeleted(), context.getId());
        return Result.ok(entity.isDeleted() ? "恢复成功!" : "删除成功!");
    }


    @PostMapping(value = "/tdelete/{id}")
    public Result tDelete(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        UserEntity entity = userService.findInternalById(id);

        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }

        if (webEname.equals(entity.getEname())) {
            log.info("系统管理员不允许删除");
            throw new ReplyCreateException(BlogBusinessExceptionCode.ADMINISTRATOR_ACCOUNTS_ARE_NOT_ALLOWED_TO_BE_DISABLED_OR_DELETED);
        }

        if (entity.isEnable()) {
            log.info("账号启用状态无法删除");
            throw new ReplyCreateException(BlogBusinessExceptionCode.ENABLED_STATE_CANNOT_BE_DELETED);
        }

        userService.delete(entity.getId());
        return Result.ok("删除成功!");
    }


    @PostMapping(value = "/edit/password")
    public Result editPassword(HttpServletRequest request, HttpSession session, @RequestBody UpdPasswordInfo updPassword) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        String verCode = (String) session.getAttribute(updPwdCodeKey);
        Result result = VerifyCodeUtils.validImage(updPassword.getPwdVerCode(), verCode, request, session, updPwdCodeKey, updPwdCodeTime);
        if (result.getCode() != 0) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.CAPTCHA_ERROR_OR_NOT_PRESENT, result.getMessage());
        }

        if (Strings.isNullOrEmpty(updPassword.getEname())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_ENAME_CANNOT_BE_EMPTY);
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
    public V2Result<UserInfo> list(Integer page, Integer limit, HttpServletRequest request) {
        AuthContext context = regularCheck(request, Boolean.TRUE, Boolean.TRUE);
        if (Objects.isNull(context)) {
            return new V2Result<>();
        }

        String keyWord = request.getParameter("title");

        Page pageEntity = new Page();
        if (Objects.nonNull(page)) {
            pageEntity.setPageNumber(page);
        }
        if (Objects.nonNull(limit)) {
            pageEntity.setPageSize(limit);
        }


        UserQuery query = new UserQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setCname(keyWord);
        }

        PageResult<UserEntity> result = userService.find(query, pageEntity);

        if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getDataList())) {
            return new V2Result<>();
        }

        List<UserEntity> dataList = result.getDataList();
        List<UserInfo> userInfos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            userInfos = UserInfo.creaetUserList(dataList);
        }

        V2Result v2Result = new V2Result();
        v2Result.setCode(0);
        v2Result.setCount(result.getTotal());
        v2Result.setData(userInfos);
        v2Result.setMsg("");
        return v2Result;
    }


    @RequestMapping("/del/list")
    @ResponseBody
    public V2Result<UserInfo> delList(Integer page, Integer limit, HttpServletRequest request) {

        AuthContext context = regularCheck(request, Boolean.TRUE, Boolean.TRUE);
        if (Objects.isNull(context)) {
            return new V2Result<>();
        }

        String keyWord = request.getParameter("title");

        Page pageEntity = new Page();
        if (Objects.nonNull(page)) {
            pageEntity.setPageNumber(page);
        }
        if (Objects.nonNull(limit)) {
            pageEntity.setPageSize(limit);
        }


        UserQuery query = new UserQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setCname(keyWord);
        }

        query.setDeleted(true);

        PageResult<UserEntity> result = userService.find(query, pageEntity);

        if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getDataList())) {
            return new V2Result<>();
        }

        List<UserEntity> dataList = result.getDataList();
        List<UserInfo> userInfos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            userInfos = UserInfo.creaetUserList(dataList);
        }

        V2Result v2Result = new V2Result();
        v2Result.setCode(0);
        v2Result.setCount(result.getTotal());
        v2Result.setData(userInfos);
        v2Result.setMsg("");
        return v2Result;
    }
}
