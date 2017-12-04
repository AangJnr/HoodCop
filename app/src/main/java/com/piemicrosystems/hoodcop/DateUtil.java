package com.piemicrosystems.hoodcop;


import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by AangJnr on 9/20/16.
 */
public class DateUtil {


    public static String getDateInMillisToString() {

        long now = System.currentTimeMillis();

        return String.valueOf(now);

    }


    public static String getFormattedDate() {


        DateFormat df = new SimpleDateFormat("yyyy.MM.dd", Locale.US);
        return df.format(Calendar.getInstance().getTime());


    }


    public static String getDateInLong() {


        DateFormat df = new SimpleDateFormat("yyyy.MM.dd", Locale.US);
        return df.format(Calendar.getInstance().getTime());


    }


    public static String getFormattedTime() {


        DateFormat df = new SimpleDateFormat(" '@' HH:mm:ss", Locale.US);
        return df.format(Calendar.getInstance().getTime());


    }


    public static String convertStringToPrettyTime(String date) {

        try {
            PrettyTime prettyTime = new PrettyTime();
            long l = Long.parseLong(date);

            return prettyTime.format(new Date(l));
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }


    public static String convertLongToPrettyTime(Long date) {

        try {
            PrettyTime prettyTime = new PrettyTime();

            return prettyTime.format(new Date(date));
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }


}








