package com.clevel.selos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
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
        if(string == null || "null".equals(string.toLowerCase())){
            return true;
        }else{
            return false;
        }
    }

    public boolean checkLength(String string, int length){
        if(null!=string){
            return string.length()<=length?true:false;
        }else{
            return false;
        }
    }
    public boolean checkSize(ArrayList arrayList, int size){
        if(null!=arrayList){
            return arrayList.size()>=size?true:false;
        }else{
            return false;
        }
    }
    public String convertNullToBlank(String string){
        if(string != null || !"null".equals(string.toLowerCase())){
            return "";
        }else{
            return string;
        }
    }
    public List convertNullToEmpeyList(List list){
        return list!=null?list: Collections.EMPTY_LIST;
    }

    public static String getLinkKey(String userId){
        return userId+"_"+ System.currentTimeMillis();
    }

}
