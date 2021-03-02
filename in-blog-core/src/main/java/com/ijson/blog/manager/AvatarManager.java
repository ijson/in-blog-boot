package com.ijson.blog.manager;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * desc: 默认分配头像,用户可在后台自行设置头像
 * <p>
 * 1.jpg               30.jpg
 * aratar_1.jpg        aratar_1665.jpg
 * version: 6.7
 * Created by cuiyongxu on 2019/12/7 12:00 AM
 */
@Slf4j
@Component
public class AvatarManager {

    @Value("${avatar.server}")
    private String cdnServer;

    private final int MAX_NUMBER = 1666;

    public String getAvatarUrl() {
        int no = RandomUtils.nextInt(MAX_NUMBER);
        if (no == 0) {
            no = no + 1 + MAX_NUMBER % RandomUtils.nextInt(50);
        }
        if (no > 30) {
            return getUrl("aratar_" + no + ".jpg");
        } else {
            if (RandomUtils.nextBoolean()) {
                return getUrl(no + ".jpg");
            } else {
                return getUrl("aratar_" + no + ".jpg");
            }
        }
    }

    private String getUrl(String aratar) {
        return cdnServer + "avatar/" + aratar;
    }
}
