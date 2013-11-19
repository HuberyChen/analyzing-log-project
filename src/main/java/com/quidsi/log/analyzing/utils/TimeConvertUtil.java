package com.quidsi.log.analyzing.utils;

import com.quidsi.core.util.DateUtils;
import com.quidsi.core.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public static List<String> getDateRange(String startDate, String endDate) {
        startDate = TimeConvertUtil.formatDate(TimeConvertUtil.stringConvertToDate(startDate));
        endDate = TimeConvertUtil.formatDate(TimeConvertUtil.stringConvertToDate(endDate));

        List<String> dates = new ArrayList<>();
        Date start = TimeConvertUtil.stringConvertToDate(startDate);
        Date end = TimeConvertUtil.stringConvertToDate(endDate);
        dates.add(TimeConvertUtil.formatDate(start));
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        while (true) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            if (end.after(cal.getTime())) {
                dates.add(TimeConvertUtil.formatDate(cal.getTime()));
            } else {
                break;
            }
        }
        if (!TimeConvertUtil.formatDate(start).equals(TimeConvertUtil.formatDate(end))) {
            dates.add(TimeConvertUtil.formatDate(end));
        }
        return dates;
    }
}
