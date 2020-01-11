package com.ijson.blog.util;

/**
 * desc:
 * version: 6.7
 * Created by cuiyongxu on 2019/12/11 8:00 PM
 */
public class RegularUtil {


    public static boolean isEmail(String email) {
        if (email == null || "".equals(email)) return false;
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }


    public static boolean isMobile(String phoneNumber) {
        if (phoneNumber == null || "".equals(phoneNumber))
            return false;
        String regex = "^1[3|4|5|8][0-9]\\d{8}$";
        return phoneNumber.matches(regex);
    }

    public static boolean isEname(String ename) {
        if (ename == null || "".equals(ename))
            return false;
        String regex = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
        return ename.matches(regex);
    }


    public static boolean isCname(String cname) {
        if (cname == null || "".equals(cname))
            return false;
        String regex = "^[a-zA-Z0-9\\[\u4e00-\u9fa5.·\u36c3\u4DAE]{0,}$";
        return cname.matches(regex);
    }

    public static void main(String[] args) {
        System.out.println(RegularUtil.isCname("你好"));
        System.out.println(RegularUtil.isCname("你好*"));
    }
}
