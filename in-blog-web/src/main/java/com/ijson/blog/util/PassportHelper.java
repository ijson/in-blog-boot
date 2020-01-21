package com.ijson.blog.util;


import com.google.common.base.Strings;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description:  公共操作类
 *
 * @author cuiyongxu 创建时间：2015-07-01
 */
public class PassportHelper {

    private static PassportHelper instance;
    private final Pattern pattern = Pattern.compile("\\S*[?]\\S*");//正则表达式

    private PassportHelper() {

    }

    //单例操作
    public synchronized static PassportHelper getInstance() {
        if (instance == null) {
            instance = new PassportHelper();
        }
        return instance;
    }

    public String getCookieName() {
        return "_blogl";
    }

    public String getRemCookieName() {
        return "__rem_l_ck";
    }

    /**
     * description:  获取连接后缀,只支持a.a不支持a.a.a
     *
     * @author xyc
     * @update 2014-12-18
     */
    public String parseSuffix(String url) {
        if (url.length() <= 1) {
            return "";
        }
        Matcher matcher = pattern.matcher(url);

        String[] spUrl = url.toString().split("/");
        int len = spUrl.length;
        String endUrl = spUrl[len - 1];

        if (matcher.find()) {
            String[] spEndUrl = endUrl.split("\\?");
            return spEndUrl[0].split("\\.")[1];
        }
        return endUrl.split("\\.")[1];
    }

    /**
     * description:  支持全部url,webservice
     *
     * @author xyc
     * @update 2014-12-24
     */
    public String parseUrlSuffix(String url) {
        int lastIndex = 0;
        String rtn = "";
        if (url.length() <= 1) {
            return rtn;
        }
        if (url.contains(".")) {
            lastIndex = url.lastIndexOf(".") + 1;
            return url.substring(lastIndex, url.length());
        }
        if (url.contains("@")) {
            lastIndex = url.lastIndexOf("@") + 1;
            return url.substring(lastIndex, url.length());
        }
        if (url.contains("#")) {
            lastIndex = url.lastIndexOf("#") + 1;
            return url.substring(lastIndex, url.length());
        }
        if (url.contains("?")) {
            lastIndex = url.lastIndexOf("?") + 1;
            return url.substring(lastIndex, url.length());
        }
        if (url.contains("!")) {
            lastIndex = url.lastIndexOf("!") + 1;
            String value = url.substring(lastIndex, url.length());
            if (value.contains(".")) {
                rtn = value.substring(value.indexOf("."), value.length());
                return rtn;
            }
        }
        return url.substring(lastIndex, url.length());
    }

    /**
     * description:  获取登录用户IP地址
     *
     * @author xyc
     * @update 2014-12-23
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            ip = "本地";
        }
        return ip;
    }

    /**
     * description:  获取当前登录用户的cookie值
     *
     * @author xyc
     * @update 2014-12-23
     */
    public String getCurrCookie(HttpServletRequest request) {
        Cookie tokenCook = null;
        Cookie[] cookies = request.getCookies();
        if (null != cookies)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(getCookieName())) {
                    tokenCook = cookie;
                    break;
                }
            }
        if (tokenCook == null) {
            return "";
        }
        return tokenCook.getValue();
    }

    public String getRemCurrCookie(HttpServletRequest request) {
        Cookie tokenCook = null;
        Cookie[] cookies = request.getCookies();
        if (null != cookies)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(getRemCookieName())) {
                    tokenCook = cookie;
                    break;
                }
            }
        if (tokenCook == null) {
            return "";
        }
        return tokenCook.getValue();
    }


    public String getCookie(HttpServletRequest request, String cookieName) {
        Cookie tokenCook = null;
        Cookie[] cookies = request.getCookies();
        if (null != cookies)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    tokenCook = cookie;
                    break;
                }
            }
        if (tokenCook == null) {
            return "";
        }
        return tokenCook.getValue();
    }

    /**
     * description: 根据key获取cookie值
     *
     * @author xyc
     * @update 2014-12-31
     */
    public String getCurrCookie(HttpServletRequest request, String key) {
        Cookie tokenCook = null;
        Cookie[] cookies = request.getCookies();
        if (null != cookies)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    tokenCook = cookie;
                    break;
                }
            }
        if (tokenCook == null) {
            return "";
        }
        return tokenCook.getValue();
    }

    /**
     * description:  获取url路径 必须为 req.getServletPath()后的路径
     *
     * @author xyc
     * @update 2014-12-24
     */
    public String parseUrlPath(String servletpath) {
        if (servletpath.length() <= 1) {
            return "";
        }
        int lastIndex = servletpath.lastIndexOf("/");
        String urlpath = servletpath.substring(0, lastIndex + 1);
        return urlpath;
    }

    private Random random = new Random();
    private char ch[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
            'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '0', '1'};

    /**
     * description:  生成指定长度的随机字符串
     *
     * @author xyc
     * @update 2014-12-22
     */
    public String createRandomString(int length) {
        if (length > 0) {
            int index = 0;
            char[] temp = new char[length];
            int num = random.nextInt();
            for (int i = 0; i < length % 5; i++) {
                temp[index++] = ch[num & 63];
                num >>= 6;
            }
            for (int i = 0; i < length / 5; i++) {
                num = random.nextInt();
                for (int j = 0; j < 5; j++) {
                    temp[index++] = ch[num & 63];
                    num >>= 6;
                }
            }
            return new String(temp, 0, length);
        } else if (length == 0) {
            return "";
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * description:  获取系统当前时间(yyyy-MM-dd HH:mm:ss)
     *
     * @author xyc
     * @update 2015-1-21
     */
    public String sysNowDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * description:  获取系统当前时间
     *
     * @param format 自定义日期格式 例如 format=yyyy-MM-dd HH:mm:ss
     * @author xyc
     * @update 2015-1-21
     */
    public String sysNowDate(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * description:  获取当前时间串
     *
     * @author xyc
     * @update 2015-1-21
     */
    public long sysNowDateTypeInt() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * description:  sha1加密
     *
     * @author xyc
     * @update 2015-2-2
     */
    public String SHA1(String decript) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte[] messageDigest = digest.digest();

            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < messageDigest.length; ++i) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * description:  直接使用对list内容进行sha1加密
     *
     * @author xyc
     * @update 2015-2-5
     */
    public String SHA1(ArrayList<String> lst) {
        try {
            Collections.sort(lst);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < lst.size(); ++i) {
                sb.append((String) lst.get(i));
            }
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(sb.toString().getBytes());
            byte[] messageDigest = digest.digest();

            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < messageDigest.length; ++i) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * description:  验证邮箱格式
     *
     * @author xyc
     * @update 2015-1-16
     */
    public boolean emailFormat(String email) {
        Pattern p = Pattern
                .compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /***
     * description:  验证手机号格式
     * @param mobile
     * @return
     * @author xyc
     * @update 2015-1-16
     */
    public boolean mobileFormat(String mobile) {
        Pattern p = Pattern.compile("^13\\d{9}||18\\d{9}||15[1,2,3,5,8,9]\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }


    public String getDomain(String domain) {
        URL url;
        try {
            url = new URL(domain);
            return url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + "/";
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeCookie(HttpServletRequest request, HttpServletResponse response) {
        removeCookie(request, response, null);
    }

    public void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        //获取cookie
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (Strings.isNullOrEmpty(cookieName)) {
                cookie.setMaxAge(0);
                cookie.setPath("/");  //路径一定要写上，不然销毁不了
                response.addCookie(cookie);
            } else {
                if (cookie.getName().equals(cookieName)) {
                    cookie.setMaxAge(0);
                    cookie.setPath("/");  //路径一定要写上，不然销毁不了
                    response.addCookie(cookie);
                }
            }

        }
    }
}
