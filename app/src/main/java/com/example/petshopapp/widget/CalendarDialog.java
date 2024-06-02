package com.example.petshopapp.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.petshopapp.tools.TimeConvert;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class CalendarDialog {
    private Date date = null;
    public void open(Context context, EditText edtTime){
        Calendar calendar = Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.YEAR,year);
                date = calendar.getTime();
                if(edtTime !=null)edtTime.setText(TimeConvert.convertJavaDate(date));
            }
        },year,month,day);
        datePickerDialog.show();
    }
    public Timestamp getTimestamp(){
        if(date==null)return null;
        Timestamp timestamp = new Timestamp(date.getTime());
        timestamp.setHours(0);
        timestamp.setMinutes(0);
        timestamp.setSeconds(0);
        timestamp.setNanos(0);
        return timestamp;
    }
    public Date getDate(){
        return date;
    }
}
