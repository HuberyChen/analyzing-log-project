package com.quidsi.log.analyzing.utils;

import com.quidsi.core.util.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeConvertUtil {
    private static final String EAST_TIMEZONE_DAY_LIGHT_SAVING = "America/New_York";

    public static Date getCurrentEasternDate() {
        Date now = Calendar.getInstance().getTime();
        TimeZone fromTimeZone = DateUtils.calendar(now).getTimeZone();
        TimeZone toTimeZone = TimeZone.getTimeZone(EAST_TIMEZONE_DAY_LIGHT_SAVING);
        Date fromDate = DateUtils.toCurrentTimeZone(now, fromTimeZone);
        int offset = toTimeZone.getRawOffset() + toTimeZone.getDSTSavings() - fromTimeZone.getRawOffset() - fromTimeZone.getDSTSavings();
        return DateUtils.add(fromDate, Calendar.MILLISECOND, offset);
    }

}
