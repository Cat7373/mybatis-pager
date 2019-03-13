package org.cat73.pager.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 时间处理工具类
 * <p><em>内部使用，不保证未来依旧存在，不建议外部使用</em></p>
 * @deprecated 考虑移除
 */
@Deprecated
public final class Times {
    private Times() {
        throw new UnsupportedOperationException();
    }

    /**
     * HH:mm:ss 的时间格式
     */
    private static final String TIME_PATTERN = "HH:mm:ss";
    /**
     * yyyy-MM-dd 的时间格式
     */
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * yyyy-MM-dd HH:mm:ss 的时间格式
     */
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    /**
     * 将一个时间格式化为字符串
     * @param date 被格式化的时间
     * @param pattern 格式化时使用的格式
     * @return 格式化结果
     */
    private static String format(LocalDateTime date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 用 yyyy-MM-dd HH:mm:ss 的格式将一个时间格式化为字符串
     * @param date 被格式化的时间
     * @return 格式化结果
     */
    public static String format(LocalDateTime date) {
        return Times.format(date, Times.DATETIME_PATTERN);
    }

    /**
     * 将一个时间格式化为字符串
     * @param date 被格式化的时间
     * @param pattern 格式化时使用的格式
     * @return 格式化结果
     */
    private static String format(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 用 yyyy-MM-dd 的格式将一个时间格式化为字符串
     * @param date 被格式化的时间
     * @return 格式化结果
     */
    public static String format(LocalDate date) {
        return Times.format(date, Times.DATE_PATTERN);
    }

    /**
     * 将一个时间格式化为字符串
     * @param date 被格式化的时间
     * @param pattern 格式化时使用的格式
     * @return 格式化结果
     */
    private static String format(LocalTime date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 用 HH:mm:ss 的格式将一个时间格式化为字符串
     * @param date 被格式化的时间
     * @return 格式化结果
     */
    public static String format(LocalTime date) {
        return Times.format(date, Times.TIME_PATTERN);
    }

    /**
     * 将一个时间格式化为字符串
     * @param date 被格式化的时间
     * @param format 用作格式化的格式
     * @return 格式化结果
     */
    private static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 用 yyyy-MM-dd HH:mm:ss 的格式将一个时间格式化为字符串
     * @param date 被格式化的时间
     * @return 格式化结果
     */
    public static String format(Date date) {
        return Times.format(date, Times.DATETIME_PATTERN);
    }
}
