package com.ijson.blog.bus.event;

import com.ijson.blog.bus.BaseMessageEvent;
import com.ijson.blog.bus.OperationType;
import com.ijson.blog.model.AuthContext;
import lombok.Data;

import java.util.List;


@Data
public class DeleteArticleUpdateTagEvent extends BaseMessageEvent {

    private String articleId;

    private List<String> tagIds;

    public static DeleteArticleUpdateTagEvent create(AuthContext context,String articleId, List<String> tagIds) {
        DeleteArticleUpdateTagEvent updateTagEvent = new DeleteArticleUpdateTagEvent();
        updateTagEvent.setArticleId(articleId);
        updateTagEvent.setContext(context);
        updateTagEvent.setTagIds(tagIds);
        return updateTagEvent;
    }


    @Override
    public OperationType getType() {
        return OperationType.delete_article_update_tag;
    }
}
