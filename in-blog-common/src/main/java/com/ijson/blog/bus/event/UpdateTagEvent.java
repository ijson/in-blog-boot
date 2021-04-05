
package com.ijson.blog.bus.event;

import com.ijson.blog.bus.BaseMessageEvent;
import com.ijson.blog.bus.OperationType;
import com.ijson.blog.model.AuthContext;
import lombok.Data;

import java.util.List;


@Data
public class UpdateTagEvent extends BaseMessageEvent {

    private String articleId;

    private String tagnames;

    private List<String> historyTagIds;

    public static UpdateTagEvent create(AuthContext context,String articleId, String tagnames, List<String> historyTagIds) {
        UpdateTagEvent updateTagEvent = new UpdateTagEvent();
        updateTagEvent.setArticleId(articleId);
        updateTagEvent.setTagnames(tagnames);
        updateTagEvent.setHistoryTagIds(historyTagIds);
        updateTagEvent.setContext(context);
        return updateTagEvent;
    }


    @Override
    public OperationType getType() {
        return OperationType.update_tag;
    }
}
