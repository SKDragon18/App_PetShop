package com.example.petshopapp.tools;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
}
