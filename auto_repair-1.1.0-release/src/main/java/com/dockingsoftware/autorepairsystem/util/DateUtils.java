/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.util;

import com.dockingsoftware.autorepairsystem.Constants;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    
    public static LocalDate Date2LocalDate(Date d) {
        if (d != null) {
            Instant instant = d.toInstant();
            return instant.atZone(ZoneId.systemDefault()).toLocalDate();
        } else {
            return null;
        }
    }
    
    public static Date LocalDate2Date(LocalDate ld) {
        if (ld != null) {
            Calendar c =  Calendar.getInstance();
            c.set(ld.getYear(), ld.getMonthValue() - 1, ld.getDayOfMonth() );
            Date date = c.getTime();
            return date;
        } else {
            return null;
        }
    }
    
    public static LocalDate String2LocalDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString, dateFormatter);
    }
    
    public static Date String2Date(String dateString, SimpleDateFormat f) throws ParseException {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        return f.parse(dateString);
    }
    
    public static String LocalDate2String(LocalDate localDate) {
        return dateFormatter.format(localDate);
    }
    
    public static String Date2String(Date date) {
        if (date == null) {
            date = new Date();
        }
        return simpleDateFormatter.format(date);
    }
    
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.PATTERN);
    private static SimpleDateFormat simpleDateFormatter = new SimpleDateFormat(Constants.PATTERN);
}
