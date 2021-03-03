package com.ijson.blog.bus.listener;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.eventbus.Subscribe;
import com.ijson.blog.bus.EventBusListener;
import com.ijson.blog.bus.IEventBus;
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
public class UpdateTagListener implements EventBusListener {


    @Autowired
    private PostService postService;

    @Autowired
    private TopicService topicService;


    /**
     * 1. 没有变化
     * 2. 删除一个历史的
     * 3. 删除一个历史的,并新增一个新的
     * 4. 熙增一个新的
     */

    @Subscribe
    public void updateTag(UpdateTagEvent event) {
        AuthContext context = event.getContext();
        String tagnames = event.getTagnames();
        String articleId = event.getArticleId();
        List<String> historyTagIds = event.getHistoryTagIds();
        List<String> tagName = new ArrayList<>(Splitter.on(",").trimResults().omitEmptyStrings().splitToList(tagnames));

        List<TopicEntity> tagHistoryEntity = topicService.findByIds(historyTagIds);
        List<String> historyTagNames = tagHistoryEntity.stream().map(TopicEntity::getTopicName).collect(Collectors.toList());

        Map<String, String> historyCname2Id = tagHistoryEntity.stream().collect(Collectors.toMap(TopicEntity::getTopicName, TopicEntity::getId, (k1, k2) -> k2));

        Collections.sort(tagName);
        Collections.sort(historyTagNames);
        // 1. 没有变化
        if (Joiner.on("_").join(historyTagNames).equals(Joiner.on("_").join(tagName))) {
            return;
        }

        //2. 删除一个历史的3,历史的 tag count -1
        Collection<String> subtract = CollectionUtils.subtract(historyTagNames, tagName);
        if (CollectionUtils.isNotEmpty(subtract)) {
            for (String removeTagName : subtract) {
                //删掉并集中的数据
                String historyTagId = historyCname2Id.get(removeTagName);
                //tag count -1
                historyTagIds.remove(historyTagId);
                topicService.dec(historyTagId);
                IEventBus.post(DeleteTagEvent.create(event.getContext(), historyTagId));
            }
        }

        // 3. 删除一个历史的,并新增一个新的,如果存在删除历史的  第2已处理
        Collection<String> newAddTag = CollectionUtils.subtract(tagName, historyTagNames);
        for (String tagname : newAddTag) {
            TopicEntity byTagName = topicService.findByTagName(tagname);
            if (Objects.nonNull(byTagName)) {
                topicService.inc(byTagName.getId());
                historyTagIds.add(byTagName.getId());
                continue;
            }
            TopicEntity tagEntity = topicService.create(context, TopicEntity.create(tagname, context));
            historyTagIds.add(tagEntity.getId());
        }

        postService.updateTagIds(context, articleId, historyTagIds);
    }


    @Override
    public OperationType getType() {
        return OperationType.update_tag;
    }
}
