package com.ijson.blog.bus;

import lombok.Data;

/**
 * www.ijson.com
 */
@Data
public abstract class BaseMessageEvent {

//    private AuthContext context;

    /**
     * 获取操作类型
     *
     * @return
     */
    public abstract OperationType getType();


}


