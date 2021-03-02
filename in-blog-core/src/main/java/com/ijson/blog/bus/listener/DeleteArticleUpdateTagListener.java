package com.ijson.blog.bus.listener;

import com.google.common.eventbus.Subscribe;
import com.ijson.blog.bus.EventBusListener;
import com.ijson.blog.bus.OperationType;
import com.ijson.blog.bus.event.DeleteArticleUpdateTagEvent;
//import com.ijson.framework.bus.EventBusListener;
//import com.ijson.framework.bus.OperationType;
import com.ijson.blog.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 *
 */
@Slf4j
@Component
public class DeleteArticleUpdateTagListener implements EventBusListener {

    @Autowired
    private TopicService topicService;

    @Subscribe
    public void deleteArticleUpdateTag(DeleteArticleUpdateTagEvent event) {
        log.info("删除文章:{},以下tag count-1:{}", event.getArticleId(), event.getTagIds());
        List<String> tagIds = event.getTagIds();
        for (String tagId : tagIds) {
            topicService.dec(tagId);
        }
    }


    @Override
    public OperationType getType() {
        return OperationType.delete_article_update_tag;
    }
}
