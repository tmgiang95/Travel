package com.example.admin.travel.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDate {
    public static String getDateString(long timestamp){
        SimpleDateFormat sdf = new SimpleDateFormat(" dd/MM/yyyy");
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis( timestamp );
//        return cal.get(Calendar.DAY_OF_MONTH) +" - "+cal.get(Calendar.MONTH) +" - "+cal.get(Calendar.YEAR);
    return sdf.format(new Date(timestamp));
    }
}
