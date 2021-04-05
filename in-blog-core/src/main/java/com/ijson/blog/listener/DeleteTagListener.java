package com.ijson.blog.listener;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.eventbus.Subscribe;
import com.ijson.blog.bus.EventBusListener;
import com.ijson.blog.bus.OperationType;
import com.ijson.blog.bus.event.DeleteTagEvent;
import com.ijson.blog.bus.event.UpdateTagEvent;
import com.ijson.blog.dao.entity.TopicEntity;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.PostService;
import com.ijson.blog.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@Component
public class DeleteTagListener implements EventBusListener {


    @Autowired
    private TopicService topicService;


    @Subscribe
    public void deelteTag(DeleteTagEvent event) {
        AuthContext context = event.getContext();
        String tagId = event.getTagId();
        TopicEntity topicEntity = topicService.find(tagId);
        if (Objects.nonNull(topicEntity)) {
            if (topicEntity.getPostCount() <= 0) {
                topicService.delete(tagId);
            }
        }
    }


    @Override
    public OperationType getType() {
        return OperationType.delete_tag_by_update;
    }
}
