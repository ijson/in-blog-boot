package com.ijson.blog.controller;

import com.google.common.collect.Maps;
import com.ijson.blog.exception.BlogBusinessException;
import com.ijson.blog.exception.BlogBusinessExceptionCode;
import com.ijson.blog.service.model.Result;
import com.ijson.blog.util.DateUtils;
import com.ijson.blog.util.PassportHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/7/28 9:30 PM
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {

//    @Autowired
//    private MailManager mailManager;

    @Value("#{'${error.notify.emails:#{null}}'.split(';')}")
    private List<String> errorNotifyEmails;

    @Value("${project.workspace:#{null}}")
    protected String projectWorkspace;

    @Value("${project.name:#{null}}")
    protected String projectName;

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e, HttpServletRequest request, HttpServletResponse response) {
        if (e instanceof BlogBusinessException) {
            BlogBusinessException blogBusinessException = (BlogBusinessException) e;
            return Result.error(blogBusinessException);
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            return Result.error(BlogBusinessExceptionCode.REQUEST_WAY_ERROR);
        } else {
//            log.error("【系统异常】", e);
//            log.debug("【系统异常】", e);
//            log.info("【系统异常】", e);
//            Map<String, Object> params = Maps.newHashMap();
            //nowTime
            //remoteIP
            //exception

//            try {
//                InetAddress addr = InetAddress.getLocalHost();
//                params.put("host", addr.getHostAddress());
//                params.put("host.name", addr.getHostName());
//            } catch (Exception e1) {
//                log.error("", e1);
//            }

//            Properties props = System.getProperties();
//            params.put("project.name", projectWorkspace);
//            InetAddress hostLANAddress = getLocalHostLanAddress();
//            if (Objects.nonNull(hostLANAddress)) {
//                params.put("os.ip", hostLANAddress.getHostAddress());
//            } else {
//                params.put("os.ip", "未知");
//            }
//            params.put("nowTime", DateUtils.getInstance().getTodayTime());
//            props.forEach((key, value) -> params.put((String) key, value));
//            if (Objects.nonNull(request)) {
//                params.put("remoteIP", PassportHelper.getIP(request));
//                params.put("remoteHost", PassportHelper.getIP(request));
//                params.put("requestedSessionId", request.getRequestedSessionId());
//                params.put("requestURI", request.getRequestURI());
//                params.put("requestURL", request.getRequestURL());
//            }

            StringWriter sw = new StringWriter();
            try (PrintWriter pw = new PrintWriter(sw);) {
                e.printStackTrace(pw);
            }
            String errorInfo = sw.toString();
            log.error("error:{}", errorInfo);
//            params.put("exception", errorInfo);
//            mailManager.sendMail("【" + projectName + "】- 系统告警", errorNotifyEmails, "system_error_email.template", params);
            return Result.error(BlogBusinessExceptionCode.SYSTEM_ERROR);
        }
    }

    private static InetAddress getLocalHostLanAddress() {
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            return InetAddress.getLocalHost();
        } catch (Exception e) {
            return null;
        }
    }

}
