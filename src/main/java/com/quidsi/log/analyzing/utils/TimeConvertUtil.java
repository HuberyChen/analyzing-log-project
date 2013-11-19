package com.quidsi.log.analyzing.utils;

import com.quidsi.core.util.DateUtils;
import com.quidsi.core.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConvertUtil {

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static Date stringConvertToDate(String date) {
        if (!StringUtils.hasText(date)) {
            return null;
        }
        String[] dateMessage = date.split("/");
        return DateUtils.date(Integer.parseInt(dateMessage[2]), Integer.parseInt(dateMessage[0]) - 1, Integer.parseInt(dateMessage[1]));
    }
}
