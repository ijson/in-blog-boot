package com.ijson.blog.service.model.info;

import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.HeaderEntity;
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
public class HeaderInfo {
    private String id;

    private String cname;

    private String code;

    private Boolean enable;

    public static List<HeaderInfo> createList(List<HeaderEntity> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Lists.newArrayList();
        }
        return dataList.stream().map(HeaderInfo::create).collect(Collectors.toList());
    }


    public static HeaderInfo create(HeaderEntity entity) {
        HeaderInfo info = new HeaderInfo();
        info.setCname(entity.getCname());
        info.setEnable(entity.isEnable());
        info.setId(entity.getId());
        info.setCode(entity.getCode());
        return info;
    }
}
