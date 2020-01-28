package com.ijson.blog.controller.admin;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.ijson.blog.controller.BaseController;
import com.ijson.blog.dao.entity.AuthEntity;
import com.ijson.blog.dao.entity.RoleEntity;
import com.ijson.blog.dao.query.AuthQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.ReplyCreateException;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.AuthInfo;
import com.ijson.blog.model.Constant;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.service.model.V2Result;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 4:32 PM
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthAction extends BaseController {


    @PostMapping(value = "/addup")
    public Result addOrUpdate(HttpServletRequest request, @RequestBody AuthEntity myEntity) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);

        if (Objects.isNull(myEntity.getOrder())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.AUTH_ORDER_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(myEntity.getEname())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.AUTH_ENAME_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(myEntity.getCname())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.AUTH_CNAME_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(myEntity.getPath())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.AUTH_LINK_CANNOT_BE_EMPTY);
        }

        if (Strings.isNullOrEmpty(myEntity.getId())) {
            return createAuth(context, myEntity);
        }

        AuthEntity entity = authService.findInternalById(myEntity.getId());

        entity.setCname(myEntity.getCname());
        entity.setEname(myEntity.getEname());
        entity.setPath(myEntity.getPath());
        entity.setFatherId(myEntity.getFatherId());
        entity.setMenuType(myEntity.getMenuType());
        entity.setOrder(myEntity.getOrder());
        entity.setShowMenu(myEntity.getShowMenu());
        authService.edit(context, entity);
        return Result.ok("更新成功!");
    }

    private Result createAuth(AuthContext context, AuthEntity myEntity) {

        AuthEntity byEname = authService.findByEname(myEntity.getEname());
        if (Objects.nonNull(byEname)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.AUTH_ENAME_ALREADY_EXIST);
        }

        AuthEntity newAuth = authService.create(context, myEntity);
        RoleEntity systemRole = roleService.findByEname(Constant.SYSTEM);
        systemRole.getAuthIds().add(newAuth.getId());
        systemRole.setVerifyCmt(false);
        systemRole.setVerify(false);
        roleService.edit(context, systemRole);
        return Result.ok("创建成功!");
    }

    @PostMapping(value = "/enable/{id}")
    public Result enable(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);

        AuthEntity entity = authService.findInternalById(id);

        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.PERMISSIONS_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }

        authService.enable(id, !entity.isEnable(), context);
        return Result.ok(entity.isEnable() ? "禁用成功!" : "启用成功!");
    }

    @PostMapping(value = "/order")
    public Result getOrder(HttpServletRequest request, @RequestBody AuthInfo info) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);

        if (Strings.isNullOrEmpty(info.getId())) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.REQUEST_PARAMETERS_ARE_INCOMPLETE);
        }

        List<AuthEntity> authEntities = authService.findChild(info.getId());
        String currentAuthId = info.getCurrentId();

        if (Objects.isNull(authEntities)) {
            return Result.ok(0);
        }

        if (Strings.isNullOrEmpty(currentAuthId)) {
            return Result.ok(authEntities.size());
        } else {
            Map<String, AuthEntity> authEntityMap = authEntities.stream().collect(Collectors.toMap(AuthEntity::getId, v -> v));
            if (authEntityMap.keySet().contains(currentAuthId)) {
                return Result.ok(authEntityMap.get(currentAuthId).getOrder());
            } else {
                return Result.ok(authEntities.size());
            }
        }
    }

    @PostMapping(value = "/delete/{id}")
    public Result delete(HttpServletRequest request, @PathVariable("id") String id) {
        AuthContext context = regularCheck(request, Boolean.FALSE, Boolean.FALSE);
        AuthEntity entity = authService.findInternalById(id);

        if (Objects.isNull(entity)) {
            throw new ReplyCreateException(BlogBusinessExceptionCode.PERMISSIONS_DOES_NOT_EXIST_OR_HAS_BEEN_DELETED);
        }

        if (entity.isEnable()) {
            log.info("权限启用状态无法删除");
            throw new ReplyCreateException(BlogBusinessExceptionCode.ENABLED_STATE_CANNOT_BE_DELETED);
        }

        authService.delete(id);
        return Result.ok("删除成功!");
    }


    @RequestMapping("/list")
    @ResponseBody
    public V2Result<AuthInfo> list(Integer page, Integer limit, HttpServletRequest request) {

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


        AuthQuery query = new AuthQuery();
        if (!Strings.isNullOrEmpty(keyWord)) {
            query.setCname(keyWord);
        }

        PageResult<AuthEntity> result = authService.find(query, pageEntity);

        if (Objects.isNull(result) || CollectionUtils.isEmpty(result.getDataList())) {
            return new V2Result<>();
        }

        List<AuthEntity> dataList = result.getDataList();
        List<AuthInfo> authInfos = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            authInfos = AuthInfo.createAuthList(dataList);
        }

        V2Result v2Result = new V2Result();
        v2Result.setCode(0);
        v2Result.setCount(result.getTotal());
        v2Result.setData(authInfos);
        v2Result.setMsg("");
        return v2Result;
    }
}