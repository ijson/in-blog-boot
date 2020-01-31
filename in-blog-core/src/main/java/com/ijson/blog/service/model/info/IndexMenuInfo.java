package com.ijson.blog.service.model.info;

import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.IndexMenuEntity;
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
public class IndexMenuInfo {
    private String id;

    private String cname;

    private String ename;

    private String path;

    private Boolean enable;

    private Integer order;

    public static List<IndexMenuInfo> createList(List<IndexMenuEntity> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Lists.newArrayList();
        }
        return dataList.stream().map(IndexMenuInfo::create).collect(Collectors.toList());
    }


    public static IndexMenuInfo create(IndexMenuEntity entity) {
        IndexMenuInfo info = new IndexMenuInfo();
        info.setCname(entity.getCname());
        info.setEnable(entity.isEnable());
        info.setId(entity.getId());
        info.setEname(entity.getEname());
        info.setPath(entity.getPath());
        info.setOrder(entity.getOrder());
        return info;
    }


    public static List<IndexMenuInfo> getDefaultIndexMenu() {
        List<IndexMenuInfo> menuInfos = Lists.newArrayList();

        menuInfos.add(IndexMenuInfo.create("", "首页", "", "/", true, 1));
        menuInfos.add(IndexMenuInfo.create("", "标签墙", "", "/", true, 2));

        return menuInfos;
    }

    public static IndexMenuInfo create(String id, String cname, String ename, String path, Boolean enable, Integer order) {
        IndexMenuInfo indexMenuInfo = new IndexMenuInfo();
        indexMenuInfo.setId(id);
        indexMenuInfo.setCname(cname);
        indexMenuInfo.setEname(ename);
        indexMenuInfo.setPath(path);
        indexMenuInfo.setEnable(enable);
        indexMenuInfo.setOrder(order);
        return indexMenuInfo;
    }
}
