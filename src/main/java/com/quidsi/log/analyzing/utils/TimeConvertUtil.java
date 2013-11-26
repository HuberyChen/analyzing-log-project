package com.quidsi.log.analyzing.utils;

import com.quidsi.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeConvertUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeConvertUtil.class);

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static Date stringConvertToDate(String date) {
        if (!StringUtils.hasText(date)) {
            return null;
        }

        Date dateFormat = null;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            dateFormat = sdf.parse(date);
        } catch (ParseException e) {
            LOGGER.info(e.getMessage());
        }

        return dateFormat;
    }

    public static List<String> getDateRange(String startDate, String endDate) {
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
