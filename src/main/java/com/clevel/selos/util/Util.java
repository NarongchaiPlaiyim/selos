package com.clevel.selos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {
    private static Logger log = LoggerFactory.getLogger(Util.class);
    private static Locale defaultLocale = new Locale("th", "EN");
    private static NumberFormat numberFormat = new DecimalFormat(",##0.00");

    public static String createDateString(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, defaultLocale);
        return sdf.format(date);
    }

    public static String createDateString(Date date) {
        return createDateString(date,"yyyy-MM-dd");
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

}
