package com.ijson.blog.service.model;

import lombok.Data;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/9/1 5:24 PM
 */
@Data
public class ConsoleData {
    private Long publishTotal;
    private Long readTotal;
    private Long commentTotal;
    private Long todayPublishTotal;

    public static ConsoleData create(Long publishTotal, Long readTotal, Long commentTotal, Long todayPublishTotal) {
        ConsoleData consoleData = new ConsoleData();
        consoleData.setCommentTotal(commentTotal);
        consoleData.setPublishTotal(publishTotal);
        consoleData.setReadTotal(readTotal);
        consoleData.setTodayPublishTotal(todayPublishTotal);
        return consoleData;
    }
}
