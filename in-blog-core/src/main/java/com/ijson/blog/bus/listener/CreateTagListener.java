package com.ijson.blog.bus.listener;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.ijson.blog.bus.EventBusListener;
import com.ijson.blog.bus.OperationType;
import com.ijson.blog.bus.event.CreateTagEvent;
import com.ijson.blog.dao.entity.TopicEntity;
import com.ijson.blog.model.AuthContext;
import com.ijson.blog.service.PostService;
import com.ijson.blog.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class CreateTagListener implements EventBusListener {


    @Autowired
    private PostService postService;

    @Autowired
    private TopicService topicService;

    @Subscribe
    public void createTag(CreateTagEvent event) {
        AuthContext context = event.getContext();
        String tagnames = event.getTagnames();
        String articleId = event.getArticleId();
        List<String> tagNameList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(tagnames);

        List<String> tagIds = Lists.newArrayList();
        for (String tagname : tagNameList) {
            TopicEntity byTagName = topicService.findByTagName(tagname);
            if (Objects.nonNull(byTagName)) {
                topicService.inc(byTagName.getId());
                tagIds.add(byTagName.getId());
                continue;
            }
            TopicEntity tagEntity = topicService.create(context, TopicEntity.create(tagname, context));
            tagIds.add(tagEntity.getId());
        }
        postService.updateTagIds(context, articleId, tagIds);

    }


    @Override
    public OperationType getType() {
        return OperationType.create_tag;
    }
}
