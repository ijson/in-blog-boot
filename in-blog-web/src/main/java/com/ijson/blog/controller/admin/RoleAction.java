package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.controller.admin.model.V2Result;
import com.ijson.blog.dao.entity.ConfigEntity;
import com.ijson.blog.dao.entity.RoleEntity;
import com.ijson.blog.dao.query.RoleQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.ReplyCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.RoleInfo;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:32 PM
 */
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleAction extends BaseController {


    @PostMapping(value = "/addup")
    public Result addOrUpdate(HttpServletRequest request, @RequestBody RoleEntity myEntity) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("未获取到当前登入人用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }

        List<String> keys = myEntity.getKeys();
        List<String> authKeys;
        if (CollectionUtils.isNotEmpty(keys)) {
            authKeys = keys.stream().filter(k -> k.length() > 20).collect(Collectors.toList());
            myEntity.setAuthIds(authKeys);
            myEntity.setKeys(null);
        }

        if (Strings.isNullOrEmpty(myEntity.getId())) {
            return create(request, myEntity);
        }

        RoleEntity entity = roleService.findInternalById(myEntity.getId());

        entity.setCname(myEntity.getCname());
        entity.setEname(myEntity.getEname());
        entity.setUserIds(myEntity.getUserIds());
        entity.setAuthIds(myEntity.getAuthIds());

        roleService.edit(context, entity);
        return Result.ok("更新成功!");
    }

    private Result create(HttpServletRequest request, RoleEntity myEntity) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("未获取到当前登入人用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        roleService.create(context, myEntity);
        return Result.ok("创建成功!");
    }

    @PostMapping(value = "/enable/{id}")
    public Result enable(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("未获取到当前登入人用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        RoleEntity entity = roleService.findInternalById(id);

        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.PERMISSIONS_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }

        ConfigEntity configEntity = webSiteService.findAllConfig();
        if (id.equals(configEntity.getRegRoleId())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.PLEASE_MODIFY_THE_WEBSITE_USER_REGISTRATION_ROLE_TO_DISABLE_OR_REMOVE);
        }

        roleService.enable(id, !entity.isEnable(), context);
        return Result.ok(entity.isEnable() ? "禁用成功!" : "启用成功!");
    }

    @PostMapping(value = "/delete/{id}")
    public Result delete(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = getContext(request);
        if (Objects.isNull(context)) {
            log.info("未获取到当前登入人用户信息");
            throw new ReplyCreateException(BlogBusinessExceptionCode.USER_INFORMATION_ACQUISITION_FAILED);
        }
        RoleEntity entity = roleService.findInternalById(id);

        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.ROLE_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }

        ConfigEntity configEntity = webSiteService.findAllConfig();
        if (id.equals(configEntity.getRegRoleId())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.PLEASE_MODIFY_THE_WEBSITE_USER_REGISTRATION_ROLE_TO_DISABLE_OR_REMOVE);
        }

        if (entity.isEnable()) {
            log.info("启用状态无法删除");
            throw new ReplyCreateException(BlogBusinessExceptionCode.ENABLED_STATE_CANNOT_BE_DELETED);
        }

        roleService.delete(id);
        return Result.ok("删除成功!");
    }


    @RequestMapping("/list")
    @ResponseBody
    public V2Result<RoleInfo> list(Integer page, Integer limit, HttpServletRequest request) {

        AuthContext context = getContext(request);
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


        RoleQuery query = new RoleQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setCname(keyWord);
        }

        PageResult<RoleEntity> result = roleService.find(query, pageEntity);

        if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getDataList())) {
            return new V2Result<>();
        }

        List<RoleEntity> dataList = result.getDataList();
        List<RoleInfo> infos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            infos = RoleInfo.createList(dataList);
        }

        V2Result v2Result = new V2Result();
        v2Result.setCode(0);
        v2Result.setCount(result.getTotal());
        v2Result.setData(infos);
        v2Result.setMsg("");
        return v2Result;
    }
}