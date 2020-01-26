package com.ijson.blog.service.model.info;

import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.RoleEntity;
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
public class RoleInfo {
    private String id;

    private String cname;

    private String ename;

    private List<String> authIds;

    private List<String> userIds;

    private Boolean enable;

    private Boolean verify;

    private Boolean verifyCmt;

    public static List<RoleInfo> createList(List<RoleEntity> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Lists.newArrayList();
        }
        return dataList.stream().map(RoleInfo::create).collect(Collectors.toList());
    }


    public static RoleInfo create(RoleEntity entity) {
        RoleInfo info = new RoleInfo();
        info.setCname(entity.getCname());
        info.setEnable(entity.isEnable());
        info.setId(entity.getId());
        info.setEname(entity.getEname());
        info.setAuthIds(entity.getAuthIds());
        info.setUserIds(entity.getUserIds());
        info.setVerify(entity.getVerify());
        info.setVerifyCmt(entity.getVerifyCmt());
        return info;
    }
}
