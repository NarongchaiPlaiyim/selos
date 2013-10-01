package com.clevel.selos.util;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.*;
import java.util.*;

public class Util {
    private static Logger log = LoggerFactory.getLogger(Util.class);
    private static Locale defaultLocale = new Locale("th", "EN");
    private static NumberFormat numberFormat = new DecimalFormat(",##0.00");

    public static String createDateString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, defaultLocale);
        return sdf.format(date);
    }

    public static String replaceStringToBlank(String str,String replaceValue){

        return str.replace(replaceValue,"");
    }
    public static String createDateString(Date date) {
        return createDateString(date,"yyyy-MM-dd");
    }
    public static Date convertStringToDateBuddhist(String dateStr) {
        Date dateD = new Date();
        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            dateD = (Date) formatter.parse(dateStr);

        } catch (ParseException e) {
        }
        return dateD;
    }

    public static Date strYYYYMMDDtoDateFormat(String dateStr){
        Date date = new Date();
        if(dateStr==null){
            return null;
        }
        try{
            DateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
            date = (Date) formatter.parse(dateStr);
        } catch (ParseException e) {
        }
        return date;
    }

    public static Date strToDateFormat(String dateStr, String format){
        Date date = new Date();
        if(dateStr==null){
            return null;
        }
        try{
            DateFormat formatter = new SimpleDateFormat(format, Locale.US);
            date = (Date) formatter.parse(dateStr);
        } catch (ParseException e) {
        }
        return date;
    }

    public static String formatNumber(double value) {
        return numberFormat.format(value);
    }

    public static String fixLength(long number,int digit) {
        return String.format("%0" + digit + "d", number);
    }

    public static boolean isEmpty(String field) {
        return (field == null) || "".equalsIgnoreCase(field.trim());
    }

    public static boolean isTrue(String str) {
        return str != null && str.matches("[tT]rue|[yY]es|1");
    }


    public static boolean isNull(String string){
        if(string == null || "null".equals(string.toLowerCase().trim())){
            return true;
        }else{
            return false;
        }
    }

    public static boolean checkLength(String string, int length){
        if(null!=string){
            return string.length()<=length?true:false;
        }else{
            return false;
        }
    }
    public static boolean checkSize(ArrayList arrayList, int size){
        if(null!=arrayList){
            return arrayList.size()>=size?true:false;
        }else{
            return false;
        }
    }
    public static String convertNullToBlank(String string){
        if(string != null || !"null".equals(string.toLowerCase())){
            return "";
        }else{
            return string;
        }
    }
    public static List convertNullToEmpeyList(List list){
        return list!=null?list: Collections.EMPTY_LIST;
    }

    public static String getLinkKey(String userId){
        return userId+"_"+ System.currentTimeMillis();
    }

    public static int getDayOfDate(Date date){
        int day = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        day = cal.get(Calendar.DAY_OF_MONTH);

        return day;
    }

    public static int getMonthOfDate(Date date){
        int month = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        month = cal.get(Calendar.MONTH) + 1;

        return month;
    }
}
