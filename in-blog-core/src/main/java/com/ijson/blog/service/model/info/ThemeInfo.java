package com.ijson.blog.service.model.info;

import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.ThemeEntity;
import com.ijson.blog.dao.model.ViewOrAdminType;
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
public class ThemeInfo {
    private String id;
    private String cname;

    private String ename;

    private ViewOrAdminType type;

    private String desc;

    private boolean enable;

    private String createdBy;

    private long createTime;

    private long lastModifiedTime;

    private String lastModifiedBy;

    private String viewTheme;

    private String adminTheme;

    public static List<ThemeInfo> createList(List<ThemeEntity> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Lists.newArrayList();
        }
        return dataList.stream().map(ThemeInfo::create).collect(Collectors.toList());
    }


    public static ThemeInfo create(ThemeEntity entity) {
        ThemeInfo info = new ThemeInfo();
        info.setId(entity.getId());
        info.setCname(entity.getCname());
        info.setEname(entity.getEname());
        info.setType(entity.getType());
        info.setDesc(entity.getDesc());
        info.setEnable(entity.isEnable());
        return info;
    }

}
