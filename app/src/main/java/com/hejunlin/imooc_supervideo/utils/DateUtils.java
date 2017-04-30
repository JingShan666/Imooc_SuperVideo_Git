package com.hejunlin.imooc_supervideo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hejunlin on 17/4/29.
 */

public class DateUtils {

    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String str = format.format(curDate);
        return str;
    }

}
