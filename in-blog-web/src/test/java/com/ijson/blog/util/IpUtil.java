package com.ijson.blog.util;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 获取ip控制器
 * @ClassName: IpController.java
 */

@Slf4j
public class IpUtil {


    @Test
    public void getBrowser(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        log.info("******************************");
        log.info("操作系统及浏览器信息：" + ua);
        //转成UserAgent对象
        UserAgent userAgent = UserAgent.parseUserAgentString(ua);
        //获取浏览器信息
        Browser browser = userAgent.getBrowser();
        log.info("浏览器信息：" + browser);
        //获取系统信息
        OperatingSystem os = userAgent.getOperatingSystem();
        log.info("系统信息：" + os);
        //系统名称
        String system = os.getName();
        log.info("系统名称：" + system);
        //浏览器名称
        String browserName = browser.getName();
        log.info("浏览器名称：" + browserName);
        log.info("******************************");

    }


}