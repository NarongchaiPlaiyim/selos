package com.clevel.selos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ValidationUtil {
    private static Logger log = LoggerFactory.getLogger(ValidationUtil.class);

    public static boolean isValueInRange(int min,int max,int value) {
        return value >= min && value <= max;
    }

    public static boolean isGreaterThan(int max,int value) {
        return value > max;
    }

    public static boolean isLessThan(int min,int value) {
        return  value < min;
    }
    public static boolean isEqualRange(int min,int value) {
        return  value == min;
    }


    //for NCB
    public static boolean isEmpty(String field) {
        return (field == null) || "".equalsIgnoreCase(field.trim());
    }
    public static boolean isNull(String string){
        if(string == null || "null".equals(string.toLowerCase().trim())){
            return true;
        }else{
            return false;
        }
    }
    public static boolean isValueInRange(int min,int max,String string) {
        if (!isNull(string)) {
            return (string.length() >= min) && (string.length() <= max);
        } else {
            return false;
        }
    }

    public static boolean isValueInRange(int min,int max,ArrayList arrayList) {
        if(null!=arrayList) {
            return (arrayList.size() >= min) && (arrayList.size() <= max);
        } else {
            return false;
        }
    }

    public static boolean isGreaterThan(int max,String string) {
        if (!isNull(string)) {
            return max < string.length();
        } else {
            return true;
        }
    }
    public static boolean isLessThan(int min,String string) {
        if (!isNull(string)) {
            return  string.length() < min;
        } else {
            return true;
        }
    }
    public static boolean isLessThan(int min,ArrayList arrayList) {
        if (arrayList!=null) {
            return  arrayList.size() < min;
        } else {
            return true;
        }
    }
    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }


}
