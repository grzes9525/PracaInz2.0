package com.pracainz20.Util;


import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Grzechu on 12.01.2018.
 */

public abstract class CalendarManagement {


    public CalendarManagement() {
    }

    public static String getDate(Integer day) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY");
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, day);
        dt = c.getTime();
        //System.out.println(dateFormat.format(dt));
        String formattedDate;
        if (day == 1) {
            formattedDate = "jutro";
        } else if (day == -1) {
            formattedDate = "wczoraj";
        } else if (day == 0) {
            formattedDate = "dzisiaj";
        } else {
            formattedDate = dateFormat.format(dt);
        }

        return formattedDate;
    }

    public static String getDateToSaveDatabase(Integer day) {
        String firstDate = null;
        String secondDate = null;
        String thirdDate = null;


        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, day);
        dt = c.getTime();

        firstDate = dt.toString().substring(4, 7);
        secondDate = dt.toString().substring(8, 10);
        thirdDate = dt.toString().substring(30, 34);

        Log.d("time:", dt.toString());
        return firstDate + secondDate + thirdDate;
    }


}
