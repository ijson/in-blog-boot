package com.ijson.blog.model;

import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.AuthEntity;
import com.ijson.blog.model.arg.AuthArg;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

    private boolean checked;

    private boolean disabled;

    private String currentId;

    private boolean showMenu;

    public static List<AuthInfo> createAuthList(List<AuthEntity> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Lists.newArrayList();
        }
        return dataList.stream().map(AuthInfo::create).collect(Collectors.toList());
    }


    public static AuthInfo create(AuthEntity entity) {
        return create(entity, false, false);
    }

    public static AuthInfo create(AuthEntity entity, boolean checked, boolean disabled) {
        AuthInfo info = new AuthInfo();
        info.setCname(entity.getCname());
        info.setEnable(entity.isEnable());
        info.setId(entity.getId());
        info.setEname(entity.getEname());
        info.setPath(entity.getPath());
        info.setFatherId(entity.getFatherId());
        info.setMenuType(entity.getMenuType());
        info.setOrder(entity.getOrder());
        info.setChecked(checked);
        info.setDisabled(disabled);
        info.setShowMenu(entity.getShowMenu());
        return info;
    }


    public static Map<AuthArg, List<AuthInfo>> getAuthMap(List<AuthEntity> allAuth, List<String> authIds, boolean disabled) {
        List<AuthEntity> fatherEntity = allAuth.stream().filter(k -> {
            return k.getFatherId().equals("0");
        }).sorted(Comparator.comparing(AuthEntity::getOrder)).collect(Collectors.toList());

        return fatherEntity.stream().collect(Collectors.toMap(key -> {
            AuthArg arg = new AuthArg(key.getId(), key.getEname(), key.getCname(), key.getPath(), authIds.contains(key.getId()), disabled);
            arg.setOrder(key.getOrder());
            return arg;
        }, value -> {
            List<AuthInfo> vdata = allAuth.stream().filter(vs -> {
                return vs.getFatherId().equals(value.getId());
            }).collect(Collectors.toList()).stream().map(k -> {
                return AuthInfo.create(k, authIds.contains(k.getId()), disabled);
            }).sorted(Comparator.comparing(AuthInfo::getOrder)).collect(Collectors.toList());
            return vdata;
        }));
    }
}
