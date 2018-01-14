package com.pracainz20.Util;

import java.util.Date;

/**
 * Created by Grzechu on 12.01.2018.
 */

public class CalendarManagement {
    public CalendarManagement() {
    }

    public String getDate(Integer day){
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formattedDate;
        if(day==1){
            formattedDate="jutro";
        }else if(day==-1){
            formattedDate="wczoraj";
        }else if(day==0){
            formattedDate="dzisiaj";
        }else {
            formattedDate = dateFormat.format(new Date(Long.valueOf(String.valueOf(java.lang.System.currentTimeMillis()
                    +(day*86400000)))).getTime());
        }

        return formattedDate;
    }
}
