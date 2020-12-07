package com.fighter.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fighter_lee on 19/08/22.
 */
public class TimeUtil {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * 获取当前时间
     */
    public static String getLogTime() {
        return "_" + formatTime(System.currentTimeMillis()) + "_";
    }

    /**
     * 获取时间戳，单位：秒
     * 1. 物联网web接口的单位是秒
     */
    public static long getSecondTime() {
        return System.currentTimeMillis() / 1000;
    }

    public static String formatTime(long timeInMills) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timeInMills));
    }

    public static String formatTime(long timeInMills, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date(timeInMills));
    }

}
