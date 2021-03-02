package com.ijson.blog.bus.event;

import com.ijson.blog.bus.BaseMessageEvent;
import com.ijson.blog.bus.OperationType;
import com.ijson.blog.model.AuthContext;
import lombok.Data;


@Data
public class CreateTagEvent extends BaseMessageEvent {

    private String articleId;

    private String tagnames;

    public static CreateTagEvent create(AuthContext context, String articleId, String tagnames) {
        CreateTagEvent createTagEvent = new CreateTagEvent();
        createTagEvent.setArticleId(articleId);
        createTagEvent.setContext(context);
        createTagEvent.setTagnames(tagnames);
        return createTagEvent;
    }


    @Override
    public OperationType getType() {
        return OperationType.create_tag;
    }
}
