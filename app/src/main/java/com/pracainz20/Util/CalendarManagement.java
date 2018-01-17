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
                    ))+((day+1)*86400000)).getTime());
        }

        return formattedDate;
    }

    public String getDateToSaveDatabase(Integer day){
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();

        String data_without_space = (dateFormat.format(new Date(Long.valueOf(String.valueOf(java.lang.System.currentTimeMillis()))+((day+1)*86400000)).getTime())).replaceAll("\\s","");
        String data_without_comma = data_without_space.replaceAll(",","");
        return data_without_comma;
    }


}
