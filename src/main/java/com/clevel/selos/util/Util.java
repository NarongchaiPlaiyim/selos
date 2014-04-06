package com.clevel.selos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.*;
import java.util.*;

public class Util implements Serializable {
    private static Logger log = LoggerFactory.getLogger(Util.class);
    private static Locale defaultLocale = new Locale("th", "EN");
    private static NumberFormat numberFormat = new DecimalFormat(",##0.00");
    public static BigDecimal ONE_HUNDRED = new BigDecimal("100.00");

    public static String createDateString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, defaultLocale);
        return sdf.format(date);
    }

    public static String replaceStringToBlank(String str, String replaceValue) {
        return str.replace(replaceValue, "");
    }

    public static String replaceToBlank(String input, String strToReplace) {
        if (input == null) return "";
        if (strToReplace == null) return input;
        String[] target = strToReplace.split("\\|");
        for (String aTarget : target) {
            input = input.replaceAll(aTarget, "");
        }
        return input;
    }

    public static String createDateString(Date date) {
        return createDateString(date, "yyyy-MM-dd");
    }

    public static String createDateTime(Date date) {
        return createDateString(date, "ddMMyyyyyHHmmss");
    }

    public static String createDateTh(Date date) {
        return createDateString(date, "dd MM yyyy");
    }


    public static Date strYYYYMMDDtoDateFormat(String dateStr) {
        Date date = null;
        if (dateStr == null) {
            return null;
        }
        try {
            DateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.US);
            date = (Date) formatter.parse(dateStr);
        } catch (ParseException e) {
            log.error("", e);
        }
        return date;
    }

    public static Date strYYYYMMtoDateFormat(String dateStr) {
        Date date = new Date();
        if (dateStr == null) {
            return null;
        }
        try {
            DateFormat formatter = new SimpleDateFormat("yyyyMM", Locale.US);
            date = (Date) formatter.parse(dateStr);
        } catch (ParseException e) {
            log.error("", e);
        }
        return date;
    }

    public static Date strToDateFormat(String dateStr, String format) {
        Date date = new Date();
        if (dateStr == null) {
            return null;
        }
        try {
            DateFormat formatter = new SimpleDateFormat(format, Locale.US);
            date = (Date) formatter.parse(dateStr);
        } catch (ParseException e) {
            log.error("", e);
        }
        return date;
    }

    public static String formatNumber(double value) {
        return numberFormat.format(value);
    }

    public static String fixLength(long number, int digit) {
        return String.format("%0" + digit + "d", number);
    }

    public static boolean isEmpty(String field) {
        return (field == null) || "".equalsIgnoreCase(field.trim());
    }

    public static boolean isTrue(String str) {
        return str != null && str.trim().matches("[tT]rue|[yY]es|1");
    }

    public static boolean isTrue(int value) {
        if (value == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static int isTrue(boolean value) {
        return value == true ? 1 : 0 ;
    }

    public static boolean isRadioTrue(int value) {
        if (value == 2) {
            return true;
        } else {
            return false;
        }
    }

    public static String getLinkKey(String userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    public static String getDateOrTime(String date, boolean flag) {
        if (15 == date.length()) {
            if (flag) {
                String yyyy = date.substring(0, 4);
                String mm = date.substring(4, 6);
                String dd = date.substring(6, 8);
                return yyyy + "-" + mm + "-" + dd;
            } else {
                String hh = date.substring(9, 11);
                String mm = date.substring(11, 13);
                String ss = date.substring(13, 15);
                return hh + ":" + mm + ":" + ss;
            }
        } else {
            return "";
        }
    }

    public static String convertCharset(String string) throws Exception {
        return new String(string.getBytes("ISO-8859-1"), "UTF-8");
    }

    public static String setRequestNo(String appRefNumber, int count) {
        count++;
        if (count <= 9) {
            return "SL" + appRefNumber + "0" + count;
        } else {
            return "SL" + appRefNumber + count;
        }
    }

    public static String getCurrentPage() {
        String requestURL = FacesUtil.getRequest().getRequestURL().toString();
        String url = requestURL.substring(0, requestURL.lastIndexOf("/"));
        String page = "";
        if (url != null) {
            page = requestURL.replace(url, "").replace("/", "");
        }
        return page;
    }

    public static void listFields(HashMap<String, String> fields) {
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            log.debug("key: {}, value: {}", entry.getKey(), entry.getValue());
        }
    }

    public static int calAge(Date date) {
        if(date != null){
            int age = 0;
            Calendar dob = Calendar.getInstance();
            dob.setTime(date);
            Calendar today = Calendar.getInstance();
            if(dob.after(today))
                return age;
            age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
                age--;
            return age;
        } else {
            return 0;
        }
    }

    public static String[] splitSpace(String str) {
        if (str != null) {
            return str.split(" ");
        }
        return null;
    }

    public static BigDecimal add(BigDecimal value, BigDecimal augend) {
        if (value == null && augend == null)
            return null;

        if (value == null)
            return augend;

        if (augend == null)
            return value;

        return value.add(augend);
    }

    public static BigDecimal subtract(BigDecimal value, BigDecimal subtrahend) {
        if (value == null && subtrahend == null)
            return null;

        if (value == null)
            return subtrahend;

        if (subtrahend == null)
            return value;

        return value.subtract(subtrahend);
    }

    public static BigDecimal divide(BigDecimal value, BigDecimal divisor) {
        if (value == null || divisor == null)
            return null;

        if (BigDecimal.ZERO.compareTo(divisor) == 0) {
            log.debug("divide() divisor is zero!");
            return BigDecimal.ZERO;
        }

        try {
            return value.divide(divisor, 4, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("", e);
            return BigDecimal.ZERO;
        }
    }

    public static BigDecimal divide(BigDecimal value, int divisor) {
        if (value == null)
            return null;

        if (divisor == 0) {
            log.debug("divide() divisor is zero!");
            return BigDecimal.ZERO;
        }
        try {
            return value.divide(BigDecimal.valueOf(divisor), 4, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("", e);
            return BigDecimal.ZERO;
        }
    }

    public static BigDecimal multiply(BigDecimal value, BigDecimal multiplier){
        if(value == null || multiplier == null)
            return null;

        try {
            return value.multiply(multiplier);
        } catch (Exception e){
            return null;
        }
    }

    public static BigDecimal multiply(BigDecimal value, int multiplier){
        if(value == null)
            return null;

        try {
            return value.multiply(BigDecimal.valueOf(multiplier));
        } catch (Exception e){
            return null;
        }
    }

    public static BigDecimal compareToFindLower(BigDecimal b1, BigDecimal b2) {
        if (b1 == null) {
            b1 = BigDecimal.ZERO;
        }
        if (b2 == null) {
            b2 = BigDecimal.ZERO;
        }

        if (b1.compareTo(b2) > 0) {
            return b2;
        } else {
            return b1;
        }
    }

    public static <T> List<T> safetyList(List<T> list) {
        return list == null ? Collections.<T>emptyList() : list;
    }

    public static String removeNonDigit(String s) {
        if (s == null) return "";
        return s.replaceAll("\\D", "");
    }

    public static int returnNumForFlag(boolean flag) {
        return flag ? 1 : 0;
    }

    public static boolean isTrueForCheckBox(int value){
       return value == 2;
    }

    public static String getMessageException(Exception ex){
        String message = "";
        if(ex.getCause() != null){
            message = ex.getCause().toString();
        } else {
            message = ex.getMessage();
        }

        return message;
    }

    public static<T> boolean isNull(T object){
        if(object != null){
            return !"null".equalsIgnoreCase(object.toString().trim()) ? false : true;
        } else {
            return true;
        }
    }

    public static boolean isZero(int id){
        try {
            return id == 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean isZero(BigDecimal bigDecimal){
        try {
            return BigDecimal.ZERO.compareTo(bigDecimal) == 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean isZero(long id){
        try {
            return id == 0;
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean equals(String string, String string2){
        try{
            return string.toLowerCase().trim().equals(string2.toLowerCase().trim());
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static boolean isLengthZero(String string){
        try{
            return string.length() == 0;
        } catch (NullPointerException e) {
            return true;
        }
    }
    public static long parseLong(Object input,long defaultValue) {
    	if (input == null)
    		return defaultValue;
    	else if (input instanceof Long)
    		return (Long) input;
    	else { 
    		String inputStr = input.toString();
	    	if (isEmpty(inputStr))
	    		return defaultValue;
	    	try {
	    		return Long.parseLong(inputStr);
	    	} catch (NumberFormatException e) {
	    		return defaultValue;
	    	}
    	}
    }
    
    public static int compareLong(long l1,long l2) {
    	long value = l1 - l2;
    	if (value > 0)
    		return 1;
    	else if (value < 0)
    		return -1;
    	else
    		return 0;
    }
    public static int compareInt(int i1,int i2) {
    	int value = i1-i2;
    	if (value > 0)
    		return 1;
    	else if (value < 0)
    		return -1;
    	else
    		return 0;
    }

    public static String getStringNotNull(String string){
        if(string==null){
            return "";
        }
        return string;
    }

    public static String convertNullToZero(final String string){
        return string == null ? "0" : string;
    }

    public static String checkNullString(String value){
        if (value == null){
            return "-";
        }
        return value;
    }

    public static BigDecimal convertNullToZERO(BigDecimal value){
        if (value == null){
            return BigDecimal.ZERO;
        }
        return value;
    }

    public static boolean compareDateByMonthAndYear(Date date1, Date date2) {
        Calendar d1 = Calendar.getInstance();
        d1.setTime(date1);
        Calendar d2 = Calendar.getInstance();
        d2.setTime(date2);
        log.debug("compareDateByMonthAndYear() d1.month: {}, d2.month: {}", d1.get(Calendar.MONTH), d2.get(Calendar.MONTH));
        if (d1.get(Calendar.MONTH) != d2.get(Calendar.MONTH)) {
            return false;
        }
        else if (d1.get(Calendar.YEAR) != d2.get(Calendar.YEAR)) {
            return false;
        }
        return true;
    }
}