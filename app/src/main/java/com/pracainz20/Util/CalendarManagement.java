package com.pracainz20.Util;


import android.util.Log;

import java.util.Date;

/**
 * Created by Grzechu on 12.01.2018.
 */

public abstract class CalendarManagement {



    public CalendarManagement() {
    }

    public static String getDate(Integer day){
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
                    ))+((day)*86400000-3600000)).getTime());
        }

        return formattedDate;
    }

    public static String getDateToSaveDatabase(Integer day){
        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();

        String data;

        data = (dateFormat.format(new Date(Long.valueOf(String.valueOf(java.lang.System.currentTimeMillis()))+(day*86400000-3600000)).getTime())).replaceAll("\\s","");
        //data_without_comma = data_without_space.replaceAll(",","");

        //Log.d("Data",data);

        if(data.contains(".")){
            //Log.d("Calendar_dots",data_without_comma);
            data = data.replaceAll("\\.", "");
            //Log.d("Calendar_wdots",data_without_comma);
        }else if (data.contains("-")){
            //Log.d("Calendar_dots",data_without_comma);
            data = data.replaceAll("-","");
            //Log.d("Calendar_wdots",data_without_comma);
        }else if (data.contains(",")){
            //Log.d("Calendar_dots",data_without_comma);
            data = data.replaceAll(",","");
            //Log.d("Calendar_wdots",data_without_comma);
        }else if (data.contains("\\s")) {
            //Log.d("Calendar_dots",data_without_comma);
            data = data.replaceAll("\\s", "");
            //Log.d("Calendar_wdots",data_without_comma);
            //Log.d("Calendar",data_without_comma);

        }
        String data_subString =  data.substring(0,2);
        //System.out.println(data_subString);
        // Log.d("Calendar1",data_subString);
        String data_subString2 = data.substring(4);
        //System.out.println(data_subString2);
        // Log.d("Calendar2",data_subString2);
        if(data.charAt(2)=='0'&& data.charAt(3)=='1'){
            data = "Jan"+data_subString+data_subString2;
        }else if(data.charAt(2)=='0'&& data.charAt(3)=='2'){
            data = "Feb"+data_subString+data_subString2;
        }else if(data.charAt(2)=='0'&& data.charAt(3)=='3'){
            data = "Mar"+data_subString+data_subString2;
        }else if(data.charAt(2)=='0'&& data.charAt(3)=='4'){
            data = "Apr"+data_subString+data_subString2;
        }else if(data.charAt(2)=='0'&& data.charAt(3)=='5'){
            data = "May"+data_subString+data_subString2;
        }else if(data.charAt(2)=='0'&& data.charAt(3)=='6'){
            data = "June"+data_subString+data_subString2;
        }else if(data.charAt(2)=='0'&& data.charAt(3)=='7'){
            data = "July"+data_subString+data_subString2;
        }else if(data.charAt(2)=='0'&& data.charAt(3)=='8'){
            data = "Aug"+data_subString+data_subString2;
        }else if(data.charAt(2)=='0'&& data.charAt(3)=='9'){
            data = "Sept"+data_subString+data_subString2;
        }else if(data.charAt(2)=='1'&& data.charAt(3)=='0'){
            data = "Oct"+data_subString+data_subString2;
        }else if(data.charAt(2)=='1'&& data.charAt(3)=='1'){
            data = "Nov"+data_subString+data_subString2;
        }else if(data.charAt(2)=='1'&& data.charAt(3)=='2'){
            data = "Dec"+data_subString+data_subString2;
        }

        return data;
    }


}
