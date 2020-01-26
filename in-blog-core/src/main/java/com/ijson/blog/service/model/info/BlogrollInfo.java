package com.ijson.blog.service.model.info;

import com.google.common.collect.Lists;
import com.ijson.blog.dao.entity.BlogrollEntity;
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
public class BlogrollInfo {
    private String id;

    private String cname;

    private String link;

    private String imgLink;

    private Boolean enable;

    public static List<BlogrollInfo> createBlogrollList(List<BlogrollEntity> dataList) {
        if(CollectionUtils.isEmpty(dataList)){
            return Lists.newArrayList();
        }
        return dataList.stream().map(BlogrollInfo::create).collect(Collectors.toList());
    }


    public static BlogrollInfo create(BlogrollEntity blogrollEntity) {
        BlogrollInfo info = new BlogrollInfo();
        info.setCname(blogrollEntity.getCname());
        info.setEnable(blogrollEntity.isEnable());
        info.setId(blogrollEntity.getId());
        info.setImgLink(blogrollEntity.getImgLink());
        info.setLink(blogrollEntity.getLink());
        return info;
    }
}
