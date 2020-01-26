package com.ijson.blog.service.model.info;

import com.ijson.blog.dao.entity.TopicEntity;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/8/17 2:39 PM
 */
@Data
public class HotTopicInfo {
    private String id;
    private String topicName;
    private long count;
    private String ename;
    private String shamId;

    public static List<HotTopicInfo> getHotTopic(List<TopicEntity> topicEntities ){
        return topicEntities.stream().map(key -> {
            HotTopicInfo hotTopic = new HotTopicInfo();
            hotTopic.setId(key.getId());
            hotTopic.setEname(key.getEname());
            hotTopic.setShamId(key.getShamId());
            hotTopic.setCount(key.getPostCount());
            hotTopic.setTopicName(key.getTopicName());
            return hotTopic;
        }).sorted((o1, o2) -> {
            return o1.getCount() > o2.getCount() ? -1 : 1;
        }).collect(Collectors.toList());
    }
}
