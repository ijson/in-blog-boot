package com.ijson.blog.bus.event;

import com.ijson.blog.bus.BaseMessageEvent;
import com.ijson.blog.bus.OperationType;
import com.ijson.blog.model.AuthContext;
import lombok.Data;

import java.util.List;


@Data
public class DeleteTagEvent extends BaseMessageEvent {

    private String tagId;

    public static DeleteTagEvent create(AuthContext context, String tagId) {
        DeleteTagEvent updateTagEvent = new DeleteTagEvent();
        updateTagEvent.setTagId(tagId);
        updateTagEvent.setContext(context);
        return updateTagEvent;
    }


    @Override
    public OperationType getType() {
        return OperationType.delete_tag_by_update;
    }
}
