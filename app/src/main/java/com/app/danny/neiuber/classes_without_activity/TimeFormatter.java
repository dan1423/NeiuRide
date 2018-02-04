package com.app.danny.neiuber.classes_without_activity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by danny on 1/14/18.
 */

public class TimeFormatter {

    private long fTime;

    public TimeFormatter() {

    }

    public String timeToHoursMinutesSeconds(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
       return df.format(date.getTime());
    }

    public String timeToYearMonthDay(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date.getTime());
    }


}
