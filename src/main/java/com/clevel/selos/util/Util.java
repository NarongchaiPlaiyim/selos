package com.clevel.selos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public static Date convertStringToDateBuddhist(String dateStr) {
        Date dateD = new Date();
        try {
            DateFormat formatter;
            formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            dateD = (Date) formatter.parse(dateStr);

        } catch (ParseException e) {
            log.error("", e);
        }
        return dateD;
    }

    public static Date strYYYYMMDDtoDateFormat(String dateStr) {
        Date date = new Date();
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

    public static boolean isNull(String string) {
        if (string == null || "null".equals(string.toLowerCase().trim())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkLength(String string, int length) {
        if (null != string) {
            return string.length() <= length ? true : false;
        } else {
            return false;
        }
    }

    public static boolean checkSize(ArrayList arrayList, int size) {
        if (null != arrayList) {
            return arrayList.size() >= size ? true : false;
        } else {
            return false;
        }
    }

    public static String convertNullToBlank(String string) {
        if (string != null || !"null".equals(string.toLowerCase())) {
            return "";
        } else {
            return string;
        }
    }

    public static List convertNullToEmptyList(List list) {
        return list != null ? list : Collections.EMPTY_LIST;
    }

    public static String getLinkKey(String userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    public static int getDayOfDate(Date date) {
        int day = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        day = cal.get(Calendar.DAY_OF_MONTH);

        return day;
    }

    public static int getMonthOfDate(Date date) {
        int month = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        month = cal.get(Calendar.MONTH) + 1;

        return month;
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
        int resultDay = 0;
        Calendar nowDay = Calendar.getInstance();
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTime(date);
        if (birthDay.after(nowDay))
            return resultDay;

        resultDay = (nowDay.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR));
        return resultDay;
    }

    public static String[] splitSpace(String str) {
        if (str != null) {
            return str.split(" ");
        }
        return null;
    }

    public static BigDecimal divide(BigDecimal value, BigDecimal divisor) {
        if (BigDecimal.ZERO.compareTo(divisor) == 0) {
            log.debug("divide() divisor is zero!");
            return BigDecimal.ZERO;
        }
        try {
            return value.divide(divisor, 2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("", e);
            return BigDecimal.ZERO;
        }
    }

    public static BigDecimal divide(BigDecimal value, int divisor) {
        if (divisor == 0) {
            log.debug("divide() divisor is zero!");
            return BigDecimal.ZERO;
        }
        try {
            return value.divide(BigDecimal.valueOf(divisor), 2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.error("", e);
            return BigDecimal.ZERO;
        }
    }

    public static <T> List<T> safetyList(List<T> list) {
        return list == null ? Collections.<T>emptyList() : list;
    }
}
