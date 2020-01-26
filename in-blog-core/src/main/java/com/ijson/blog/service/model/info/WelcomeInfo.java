package com.ijson.blog.service.model.info;

import lombok.Data;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/9/1 5:24 PM
 */
@Data
public class WelcomeInfo {
    private Long publishTotal;
    private Long readTotal;
    private Long commentTotal;
    private Long todayPublishTotal;
    private Long userCount;

    public static WelcomeInfo create(Long publishTotal, Long readTotal, Long commentTotal, Long todayPublishTotal, Long userCount) {
        WelcomeInfo consoleData = new WelcomeInfo();
        consoleData.setCommentTotal(commentTotal);
        consoleData.setPublishTotal(publishTotal);
        consoleData.setReadTotal(readTotal);
        consoleData.setTodayPublishTotal(todayPublishTotal);
        consoleData.setUserCount(userCount);
        return consoleData;
    }
}
