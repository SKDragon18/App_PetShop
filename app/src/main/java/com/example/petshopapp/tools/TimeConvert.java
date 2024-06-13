package com.example.petshopapp.tools;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class TimeConvert {
    public static String convertJavaDate(Date date){
        if(date==null)return "";
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = dateFormat.format(date);
        return dateString;
    }
    public static String convertJavaDatetime(Timestamp timestamp){
        if(timestamp==null)return "";
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateString = datetimeFormat.format(timestamp);
        return dateString;
    }
    public static String convertJavaLocalDateTime(LocalDateTime localDateTime){
        if(localDateTime==null)return "";
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateString = dateFormat.format(localDateTime);
        return dateString;
    }

    public static int[] getDayMonthYearTimestamp(Timestamp timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        return new int[]{
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH)+1,//Do mont từ 0 - 11
                calendar.get(Calendar.YEAR)};
    }
}
