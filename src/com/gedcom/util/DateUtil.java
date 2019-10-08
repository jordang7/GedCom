package com.gedcom.util;

import com.gedcom.models.Individual;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public boolean isDateBeforeCurrentDate(String date, String format) {
        if(date == null || format == null){
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format.toString());
        try {
            Date dateToCompare = sdf.parse(date);
            Calendar cal_instance = Calendar.getInstance();
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern(format));
            Date currentDate = sdf.parse(now);
            return dateToCompare.before(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
      return false;
    }

/*
  public static void main(String[] args) {
        DateUtil obj = new DateUtil();
        String date = "08/10/2019";
//        System.out.println(obj.isDateBeforeCurrentDate(date,"yyyy-MM-dd"));
        System.out.println(obj.isDateBeforeCurrentDate(date,"dd/MM/yyyy"));
    }
*/
}