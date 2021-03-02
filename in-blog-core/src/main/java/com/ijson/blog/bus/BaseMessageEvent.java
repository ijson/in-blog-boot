package com.ijson.blog.bus;

import com.ijson.blog.model.AuthContext;
import lombok.Data;

/**
 * www.ijson.com
 */
@Data
public abstract class BaseMessageEvent {

    private AuthContext context;

    /**
     * 获取操作类型
     *
     * @return
     */
    public abstract OperationType getType();


}


