package com.ijson.blog.service.model;

import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.AuthEntity;
import com.ijson.blog.model.Constant;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 7.0.0
 * Created by cuiyongxu on 2020/1/25 5:23 PM
 */
@Data
public class AuthInfo {
    private String id;

    private String cname;

    private String ename;

    private String path;

    private Boolean enable;

    private String fatherId;

    private int order;

    private Constant.MenuType menuType;

    public static List<AuthInfo> createAuthList(List<AuthEntity> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Lists.newArrayList();
        }
        return dataList.stream().map(AuthInfo::create).collect(Collectors.toList());
    }


    public static AuthInfo create(AuthEntity entity) {
        AuthInfo info = new AuthInfo();
        info.setCname(entity.getCname());
        info.setEnable(entity.isEnable());
        info.setId(entity.getId());
        info.setEname(entity.getEname());
        info.setPath(entity.getPath());
        info.setFatherId(entity.getFatherId());
        info.setMenuType(entity.getMenuType());
        info.setOrder(entity.getOrder());
        return info;
    }
}
