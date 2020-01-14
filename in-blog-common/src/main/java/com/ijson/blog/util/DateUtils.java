package com.ijson.blog.util;

import com.google.common.base.Strings;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;


/**
 * 日期Util类
 *
 * @author cuiyongxu 创建时间：Oct 8, 2015
 */
public class DateUtils {

    private static String defaultDatePattern = "yyyy-MM-dd";
    private static final long oneDayMillSeconds = 24 * 60 * 60 * 1000;

    private DateUtils() {
    }

    private static DateUtils instance;

    public static DateUtils getInstance() {
        if (null == instance) {
            instance = new DateUtils();
        }
        return instance;
    }

    /**
     * 获取毫秒数
     *
     * @return now time
     * @author cuiyongxu
     */
    public long getLongTime() {
        Date today = new Date();
        return today.getTime();
    }

    /**
     * 返回预设Format的当前日期字符串
     *
     * @return String 把当前日期格式化为"yyyy-MM-dd"形式的字符串
     * @author cuiyongxu
     */
    public String getToday() {
        Date today = new Date();
        return format(today);
    }

    /**
     * 获取当前时间,yyyy-MM-dd HH:mm:ss
     *
     * @return now time
     * @author cuiyongxu
     */
    public String getTodayTime() {
        Date today = new Date();
        String timePattern = "yyyy-MM-dd HH:mm:ss";
        return format(today, timePattern);
    }

    /**
     * 使用预设Format格式化Date成字符串
     *
     * @param date 给定日期
     * @return String 将给定日期格式化为"yyyy-MM-dd"形式的字符串
     * @author cuiyongxu
     */
    public String format(Date date) {
        return date == null ? "" : format(date, defaultDatePattern);
    }

    /**
     * 使用参数Format格式化Date成字符串
     *
     * @param date    给定日期
     * @param pattern 给定的格式化字符串
     * @return String 将给定的日期按照pattern进行格式化，并返回格式化好的日期字符串。
     * @author cuiyongxu
     */
    public String format(Date date, String pattern) {
        return date == null ? "" : new SimpleDateFormat(pattern).format(date);
    }


    public Long format(String pattern, String time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        simpleDateFormat.setLenient(false);
        try {
            return simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 使用预设格式将字符串转为Date
     *
     * @param strDate 给定的字符串
     * @return Date 将给定的字符串格式化为"yyyy-MM-dd"的日期类型返回
     * @throws ParseException 异常
     */
    public Date parse(String strDate) throws ParseException {
        return Strings.isNullOrEmpty(strDate) ? null : parse(strDate, defaultDatePattern);
    }

    /**
     * 使用参数Format将字符串转为Date
     *
     * @param strDate 给定的字符串
     * @param pattern 给定的格式化格式
     * @return Date   将给定的字符串按照给定的格式格式化成日期类型返回
     * @throws ParseException 异常
     * @author cuiyongxu
     */
    public Date parse(String strDate, String pattern) throws ParseException {
        return Strings.isNullOrEmpty(strDate) ? null : new SimpleDateFormat(pattern).parse(strDate);
    }

    /**
     * 根据年月日获得指定的日期
     *
     * @param year  给定的年份
     * @param month 给定的月份
     * @param day   给定的日期
     * @return Date 根据给定的年月日返回指定的日期
     * @author cuiyongxu
     */
    public Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, 0, 0, 0);
        return cal.getTime();
    }

    /**
     * 判断给定日期是否为当月的最后一天
     *
     * @param date 给定日期
     * @return boolean 为true表示该日期为当月最后一天，为false表示该日期不是当月的最后一天。
     * @author cuiyongxu
     */
    public boolean isEndOfTheMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return cal.get(Calendar.DATE) == maxDay;
    }

    /**
     * 判断给定日期是否为当年的最后一天
     *
     * @param date 给定的日期
     * @return boolean 为true表示该日期为当年最后一天，为false表示该日期不是当年最后一天
     * @author cuiyongxu
     */
    public boolean isEndOfTheYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (11 == cal.get(Calendar.MONTH)) && (31 == cal.get(Calendar.DATE));
    }

    /**
     * 获得给定日期的月份的最后一天
     *
     * @param date 给定的日期
     * @return int 给定日期月份的最后一天
     * @author cuiyongxu
     */
    public int getLastDayOfTheMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    /**
     * 获取不含不含小时分钟秒的系统日期
     *
     * @return Date 系统当前日期，不含小时分钟秒。
     * @author cuiyongxu
     */
    public Date getSystemDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return new java.sql.Date(cal.getTime().getTime());
    }

    public Long getCurrentYYYYMMHH() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime().getTime();
    }

    /**
     * 获取系统的 Timestamp
     *
     * @return Timestamp 系统当前时间的时间戳
     * @author cuiyongxu
     */
    public Timestamp getSystemTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    public void main(String[] args) {
        Object dd = DateUtils.getInstance().format(new Date(), "yyyy");
        System.out.println(dd);
    }

    /**
     * 由某个日期，前推若干毫秒
     *
     * @param date        给定的日期
     * @param millSeconds 给定前推的秒数
     * @return Date       将给定的日期前推给定的秒数后的日期
     * @author cuiyongxu
     */
    public Date before(Date date, long millSeconds) {
        return fromLong(date.getTime() - millSeconds);
    }

    /**
     * 由某个日期，后推若干毫秒
     *
     * @param date        给定的日期
     * @param millSeconds 给定后推的秒数
     * @return Date       将给定的日期后推给定的秒数后的日期
     * @author cuiyongxu
     */
    public Date after(Date date, long millSeconds) {
        return fromLong(date.getTime() + millSeconds);
    }

    /**
     * 得到某个日期之后n天后的日期
     *
     * @param date 给定日期
     * @param nday 给定天数
     * @return Date 给定日期n天后的日期
     * @author cuiyongxu
     */
    public Date after(Date date, int nday) {
        return fromLong(date.getTime() + nday * oneDayMillSeconds);
    }

    /**
     * 得到当前日期之后n天后的日期
     *
     * @param n 给定天数
     * @return Date 当前日期n天后的日期
     * @author cuiyongxu
     */
    public Date afterNDays(int n) {
        return after(getDate(), n * oneDayMillSeconds);
    }

    /**
     * 得到当前日期n天前的日期
     *
     * @param n 给定天数
     * @return Date 当前日期n天数的日期
     * @author cuiyongxu
     */
    public Date beforeNDays(int n) {
        return beforeNDays(getDate(), n);

    }

    /**
     * 得到某个日期n天前的日期
     *
     * @param date 给定日期
     * @param n    给定天数
     * @return Date 给定日期n天前的日期
     * @author cuiyongxu
     */
    public Date beforeNDays(Date date, int n) {
        return fromLong(date.getTime() - n * oneDayMillSeconds);
    }

    /**
     * 昨天
     *
     * @return Date 昨天
     * @author cuiyongxu
     */
    public Date yesterday() {
        return before(getDate(), oneDayMillSeconds);
    }

    /**
     * 明天
     *
     * @return Date 明天
     * @author cuiyongxu
     */
    public Date tomorrow() {
        return after(getDate(), oneDayMillSeconds);
    }

    public long getA_B(Date dateA, Date dateB) {
        return dateA.getTime() - dateB.getTime();
    }

    /**
     * 获取当前系统时间
     *
     * @return Date 当前系统时间
     * @author cuiyongxu
     */
    public Date getDate() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 得到一个日期的毫秒表达
     *
     * @param date 给定日期
     * @return long 给定日期的毫秒表达值（Long型）
     * @author cuiyongxu
     */
    public long toLong(Date date) {
        return date.getTime();
    }

    /**
     * 将毫秒的日期数值转化为Date对象
     *
     * @param time 给定毫秒值
     * @return Date 把给定的time转换成日期类型
     * @author cuiyongxu
     */
    public Date fromLong(long time) {
        Date date = getDate();
        date.setTime(time);
        return date;
    }

    /**
     * 根据某个字符串得到日期对象
     *
     * @param dateStr 给定的日期字符串
     * @param fmtStr  给定的日期格式类
     * @return Date    根据日期格式类fmtStr把字符串dateStr转换成日期类型
     * @author cuiyongxu
     */
    public Date strToDate(String dateStr, FmtStr fmtStr) {
        DateFormat df = new SimpleDateFormat(fmtStr.toString());
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据某个字符串得到日期对象
     *
     * @param dateStr 给定的日期字符串
     * @param fmtStr  给定的字符串格式
     * @return Date    把dateStr日期字符串格式成fmtStr的日期类型
     * @author cuiyongxu
     */
    public Date strToDate(String dateStr, String fmtStr) {
        DateFormat df = new SimpleDateFormat(fmtStr);
        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将毫秒数值日期转化为字符串日期
     *
     * @param time   毫秒数
     * @param fmtStr 日期格式类
     * @return String 将参数time转换成格式为fmtStr的字符串日期
     * @author cuiyongxu
     */
    public String longToStr(long time, FmtStr fmtStr) {

        return format(fromLong(time), fmtStr.toString());
    }

    /**
     * 将字符串日期转化为毫秒的数值日期
     *
     * @param dateStr 字符串日期
     * @param fmtStr  日期格式类
     * @return long   将字符串日期dateStr转换成毫秒的数值日期
     * @author cuiyongxu
     */
    public long strToLong(String dateStr, FmtStr fmtStr) {
        return strToDate(dateStr, fmtStr).getTime();
    }

    /**
     * 得到环境变量中操作系统时区，即得到系统属性：user.timezone
     *
     * @return String 得到环境变量中操作系统时区，即得到系统属性：user.timezone
     * @author cuiyongxu
     */
    public String getTimeZoneOfSystem() {
        Properties sysProp = new Properties(System.getProperties());
        return sysProp.getProperty("user.timezone");
    }

    /**
     * 得到jvm中系统时区
     *
     * @return String 得到jvm中系统时区
     * @author cuiyongxu
     */
    public String getTimeZoneOfJVM() {
        return TimeZone.getDefault().getID();
    }

    /**
     * 检验当前操作系统时区是否正确。<br>
     * 判断依据：操作系统环境变量的时区和jvm得到的时区是否一致，一致则表明正确，否则错误。
     *
     * @return boolean true:正确；false:错误
     * @author cuiyongxu
     */
    public boolean checkTimeZone() {
        String sysTimeZone = getTimeZoneOfSystem();
        String jvmTimeZone = getTimeZoneOfJVM();
        return sysTimeZone != null && sysTimeZone.equals(jvmTimeZone);
    }

    /**
     * 初始化当前日期
     *
     * @return new date
     * @author cuiyongxu
     */
    public String getNow() {
        SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return simp.format(new Date());
    }

    /**
     * 格式化日期yyyy-MM-dd
     *
     * @param date new data
     * @return value
     * @author cuiyongxu
     */
    public String getDate(String date) {
        String result = "";
        if (!Strings.isNullOrEmpty(date)) {
            result = date.substring(0, 10);
        }
        return result;
    }

    /**
     * 格式化日期yyyy-MM-dd HH:mm:ss
     *
     * @param date new data
     * @return new data
     * @author cuiyongxu
     */
    public String getTime(String date) {
        String result = "";
        if (!Strings.isNullOrEmpty(date)) {
            result = date.substring(0, 19);
        }
        return result;
    }

    /**
     * 格式化日期yyyy-MM
     *
     * @param date now date
     * @return month
     * @author cuiyongxu
     */
    public String getMonth(String date) {
        String result = "";
        if (!Strings.isNullOrEmpty(date)) {
            result = date.substring(0, 7);
        }
        return result;
    }

    /**
     * 查询当前年
     *
     * @return year
     * @author cuiyongxu
     */
    public int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 查询当前月份
     *
     * @return current month
     * @author cuiyongxu
     */
    public int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 格式化当前月份
     * yyyyMM
     *
     * @return now month
     * @author cuiyongxu
     */
    public String getNowMonth() {
        int month = getCurrentMonth();
        if (month < 10) {
            return getCurrentYear() + "0" + month;
        } else {
            return getCurrentYear() + "" + month;
        }

    }

    /**
     * 获取当前日期值
     *
     * @return current day
     * @author cuiyongxu
     */
    public int getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    public Long getLongDate(String date) {
        Long result = 0L;
        try {
            SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");
            Date da = simp.parse(date);
            result = da.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 日志格式类，包含常用的日期时间格式
     *
     * @author cuiyongxu
     */
    public static class FmtStr {
        private String fmtStr;

        private FmtStr(String str) {
            this.fmtStr = str;
        }

        public String toString() {
            return this.fmtStr;
        }

        public static FmtStr yyyy = new FmtStr("yyyy");
        public static FmtStr yyyyMM = new FmtStr("yyyy-MM");
        public static FmtStr yyyyMMdd = new FmtStr("yyyy-MM-dd");
        public static FmtStr yyyyMMdd_HH = new FmtStr("yyyy-MM-dd HH");
        public static FmtStr yyyyMMdd_HHmm = new FmtStr("yyyy-MM-dd HH:mm");
        public static FmtStr yyyyMMdd_HHmmss = new FmtStr("yyyy-MM-dd HH:mm:ss");
        public static FmtStr yyyyMMdd_HHmmssSSS = new FmtStr("yyyy-MM-dd HH:mm:ss:SSS");

        public static FmtStr HHmm = new FmtStr("HH:mm");
        public static FmtStr HHmmss = new FmtStr("HH:mm:ss");
        public static FmtStr hhmmssSSS = new FmtStr("HH:mm:ss:SSS");

        public static FmtStr CN_yyyyMMdd = new FmtStr("yyyy年MM月dd日");
        /**
         * 中文格式：yyyy年MM月dd日
         */
        public static FmtStr CN_HHmmss = new FmtStr("HH时mm分ss秒");
        /**
         * 中文格式：HH时mm分ss秒
         */
        public static FmtStr CN_yyyyMMdd_HHmmss = new FmtStr("yyyy年MM月dd日 HH时mm分ss秒");
        /** 中文格式：yyyy年MM月dd日 HH时mm分ss秒 */
    }

}
