package com.ijson.blog.bus;

/**
 * www.ijson.com
 */
public interface EventBusListener {

    /**
     * 当前事件类型
     * @return
     */
    OperationType getType();

}
