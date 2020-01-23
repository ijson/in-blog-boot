package com.ijson.blog.service.impl;

import com.google.common.base.Strings;
import com.ijson.blog.dao.UserDao;
import com.ijson.blog.dao.entity.RoleEntity;
import com.ijson.blog.dao.entity.UserEntity;
import com.ijson.blog.dao.query.UserQuery;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.exception.BlogLoginException;
import com.ijson.blog.manager.AvatarManager;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.model.Constant;
import com.ijson.blog.model.Permission;
import com.ijson.blog.service.RoleService;
import com.ijson.blog.service.UserService;
import com.ijson.blog.util.DateUtils;
import com.ijson.blog.util.RegularUtil;
import com.ijson.mongo.support.model.Page;
import com.ijson.mongo.support.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/28 4:16 PM
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AvatarManager avatarManager;

    @Autowired
    private RoleService roleService;

    @Override
    public AuthContext login(String ename, String password) {

        if (Strings.isNullOrEmpty(ename)) {
            throw new BlogLoginException(BlogBusinessExceptionCode.INFORMATION_IS_INCOMPLETE);
        }
        if (Strings.isNullOrEmpty(password)) {
            throw new BlogLoginException(BlogBusinessExceptionCode.THE_PASSWORD_CANNOT_BE_EMPTY);
        }

        UserEntity entity = userDao.findByEname(ename);

        if (Objects.isNull(entity)) {
            throw new BlogLoginException(BlogBusinessExceptionCode.USER_NOT_FOUND);
        }

        if (Strings.isNullOrEmpty(entity.getPassword())) {
            throw new BlogLoginException(BlogBusinessExceptionCode.PASSWORD_NOT_FOUND);
        }

        if (!entity.getPassword().equals(password)) {
            throw new BlogLoginException(BlogBusinessExceptionCode.INVALID_CURRENT_PASSWORD);
        }

        AuthContext context = new AuthContext(entity.getId(),
                entity.getEname(),
                entity.getCname(),
                entity.getEmail(),
                entity.getMobile(),
                entity.getAvatar());

        RoleEntity role = roleService.findById(entity.getRoleId());
        if (Objects.nonNull(role)) {
            context.setRoleId(role.getId());
            context.setRoleEname(role.getEname());
            context.setRoleCname(role.getCname());
            context.setPermission(role.getPermission());

            if (CollectionUtils.isNotEmpty(role.getPermission())) {
                context.setPermissionPath(role.getPermission().stream().map(Permission::getPath).collect(Collectors.toList()));
                context.setPermissionEname(role.getPermission().stream().map(Permission::getEname).collect(Collectors.toList()));
            }
        }

        return context;
    }

    @Override
    public UserEntity reg(UserEntity entity) {


        if (Strings.isNullOrEmpty(entity.getEname())) {
            throw new BlogLoginException(BlogBusinessExceptionCode.USER_ENAME_CANNOT_BE_EMPTY);
        }

        if (entity.getEname().length() > 30) {
            throw new BlogLoginException(BlogBusinessExceptionCode.USER_NAMES_MUST_NOT_EXCEED_30_DIGITS);
        }

        if (!RegularUtil.isEname(entity.getEname())) {
            throw new BlogLoginException(BlogBusinessExceptionCode.INCORRECT_ACCOUNT_FORMAT);
        }

        if (Strings.isNullOrEmpty(entity.getCname())) {
            throw new BlogLoginException(BlogBusinessExceptionCode.USER_CNAME_CANNOT_BE_EMPTY);
        }

        if (entity.getCname().length() > 20) {
            throw new BlogLoginException(BlogBusinessExceptionCode.NICKNAME_MUST_NOT_EXCEED_20_DIGITS);
        }
        if (!RegularUtil.isCname(entity.getCname())) {
            throw new BlogLoginException(BlogBusinessExceptionCode.NICKNAME_FORMAT_INCORRECT);
        }

        if (Strings.isNullOrEmpty(entity.getEmail())) {
            throw new BlogLoginException(BlogBusinessExceptionCode.USER_EMAIL_CANNOT_BE_EMPTY);
        }

        if (!RegularUtil.isEmail(entity.getEmail())) {
            throw new BlogLoginException(BlogBusinessExceptionCode.INCORRECT_MAILBOX_FORMAT);
        }

        if (Strings.isNullOrEmpty(entity.getPassword())) {
            throw new BlogLoginException(BlogBusinessExceptionCode.USER_PAASWORD_CANNOT_BE_EMPTY);
        }

        UserEntity user = userDao.findByEname(entity.getEname());
        if (Objects.nonNull(user)) {
            throw new BlogLoginException(BlogBusinessExceptionCode.USER_ALREADY_EXISTS);
        }
        entity.setAvatar(avatarManager.getAvatarUrl());
        entity.setLastModifiedTime(System.currentTimeMillis());
        entity.setCreateTime(System.currentTimeMillis());
        entity.setDeleted(false);
        entity.setEnable(true);
        entity.setLastModifiedTime(System.currentTimeMillis());
        return userDao.create(entity);
    }

    @Cacheable(value = "userInfo", key = "#ename")
    @Override
    public UserEntity findUserByEname(String ename, String email, String mobile) {
        UserEntity user = userDao.findByEname(ename);
        log.info("findUserByEname{}", ename);
        return user;
    }

    @Override
    public UserEntity findUserById(String userId) {
        return userDao.findById(userId);
    }

    @CachePut(value = "userInfo", key = "#entity.ename")
    @Override
    public UserEntity edit(UserEntity entity) {
        return userDao.update(entity);
    }

    @Override
    public Map<String, String> findCnameByIds(Set<String> userIds) {
        return userDao.batchCnameByIds(userIds);
    }

    @Override
    public PageResult<UserEntity> find(UserQuery iquery, Page page) {
        PageResult<UserEntity> postEntityPageResult = userDao.find(iquery, page);
        Set<String> roleIds = postEntityPageResult.getDataList().stream().map(UserEntity::getRoleId).collect(Collectors.toSet());
        List<RoleEntity> roles = roleService.findByIds(roleIds);

        Map<String, String> roleIdOoCname = roles.stream().collect(Collectors.toMap(RoleEntity::getId, RoleEntity::getCname));


        List<UserEntity> lastEntity = postEntityPageResult.getDataList().stream()
                .peek(key -> {
                    String cname = roleIdOoCname.get(key.getRoleId());
                    key.setRoleCname(Strings.isNullOrEmpty(cname) ? Constant.UnknownRole : cname);
                }).collect(Collectors.toList());
        postEntityPageResult.setDataList(lastEntity);
        return postEntityPageResult;
    }

}
