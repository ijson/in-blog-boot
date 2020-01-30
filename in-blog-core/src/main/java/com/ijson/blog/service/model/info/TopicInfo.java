package com.ijson.blog.service.model.info;

import com.ijson.blog.dao.entity.TopicEntity;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/20 11:57 AM
 */
@Data
public class TopicInfo {
    private String id;
    private String name;
    private String ename;
    private String shamId;
    private String topicName;
    private long postCount;
    private String lastModifiedBy;
    private Boolean deleted;
    private boolean enable;
    private String createdBy;
    private long createTime;
    private long lastModifiedTime;

    public static List<TopicInfo> createList(List<TopicEntity> dataList) {
        return dataList.stream().map(k -> create(k)).collect(Collectors.toList());
    }

    public static TopicInfo create(TopicEntity topicEntity) {
        TopicInfo topicInfo = new TopicInfo();
        topicInfo.setId(topicEntity.getId());
        topicInfo.setName(topicEntity.getTopicName());
        topicInfo.setEname(topicEntity.getEname());
        topicInfo.setShamId(topicEntity.getShamId());
        topicInfo.setTopicName(topicEntity.getTopicName());
        topicInfo.setPostCount(topicEntity.getPostCount());
        topicInfo.setLastModifiedBy(topicEntity.getLastModifiedBy());
        topicInfo.setDeleted(topicEntity.getDeleted());
        topicInfo.setEnable(topicEntity.isEnable());
        topicInfo.setCreatedBy(topicEntity.getCreatedBy());
        topicInfo.setCreateTime(topicEntity.getCreateTime());
        topicEntity.setLastModifiedTime(topicEntity.getLastModifiedTime());
        return topicInfo;
    }
}
