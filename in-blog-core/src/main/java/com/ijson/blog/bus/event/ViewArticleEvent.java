package com.ijson.blog.bus.event;

import com.ijson.blog.bus.BaseMessageEvent;
import com.ijson.blog.bus.OperationType;
import com.ijson.blog.model.AuthContext;
import lombok.Data;


@Data
public class ViewArticleEvent extends BaseMessageEvent {

    private String articleId;

    public static ViewArticleEvent view(AuthContext context, String articleId) {
        ViewArticleEvent updateTagEvent = new ViewArticleEvent();
        updateTagEvent.setArticleId(articleId);
        updateTagEvent.setContext(context);
        return updateTagEvent;
    }


    @Override
    public OperationType getType() {
        return OperationType.view_article;
    }
}
