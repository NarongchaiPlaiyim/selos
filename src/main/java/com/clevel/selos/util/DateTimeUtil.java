package com.clevel.selos.util;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateTimeUtil {
    private static Logger log = LoggerFactory.getLogger(DateTimeUtil.class);
    private static final Locale defaultLocale = new Locale("th", "EN");

    public static final String defaultDateFormat = "dd/MM/yyyy";

    private static final Locale THAI_LOCALE = new Locale("th", "TH");

    public static int compareDate(Date targetDate,Date referenceDate) {
        DateTime referenceDateTime = new DateTime(getOnlyDate(referenceDate));
        DateTime targetDateTime = new DateTime(getOnlyDate(targetDate));
        return targetDateTime.compareTo(referenceDateTime);
    }

    public static Date checkMaxDate(Date targetDate){
        Date maxDate = DateTime.now().toDate();
        if(compareDate(targetDate, maxDate) > 0){
            return maxDate;
        } else {
            return targetDate;
        }
    }

    public static Date convertToDateUS(Date date){
        DateTime dateConvert = new DateTime(date, ISOChronology.getInstance());
        return dateConvert.toDate();
    }

    public static Date convertToDateTH(Date date){
        DateTime dateConvert = new DateTime(date, BuddhistChronology.getInstance());
        log.debug("dateConvert : {}", dateConvert);
        log.debug("dateConvert.toDate() : {}", dateConvert.toDate());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = simpleDateFormat.format(dateConvert.toDate());
        log.debug("Date String TH : {}", dateString);
        try{
            Date dateTH = simpleDateFormat.parse(dateConvert.toString("dd/MM/yyyy"));
            log.debug("Date TH : {}", dateTH);
            return dateTH;
        } catch (ParseException e){
            log.error("error, parsing date");
        }
        return new Date();
    }

    public static Date convertStringToDate(String date, Locale locale){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", locale);
        Date dateConvert = new Date();
        try {
            dateConvert = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            log.error("Error to parsing date. {}", date);
        }
        return dateConvert;
    }

    public static String convertDateToString(Date date, Locale locale){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", locale);
        String dateConvert = "";
        try {
            dateConvert = simpleDateFormat.format(date);
        } catch (Exception e) {
            log.error("Error to parsing date. {}", date);
        }
        return dateConvert;
    }

    public static Date getOnlyDate(Date date) {
        return getOnlyDatePlusDay(date, 0);
    }

    public static Date getOnlyDatePlusDay(Date date, int dayAdd) {
        DateTime dt = new DateTime(date);
        dt = dt.plusDays(dayAdd).dayOfMonth().roundFloorCopy();
        return dt.toDate();
    }

    public static Date getOnlyDatePlusMonth(Date date, int monthAdd) {
        DateTime dt = new DateTime(date);
        dt = dt.plusMonths(monthAdd).dayOfMonth().roundFloorCopy();
        return dt.toDate();
    }

    public static Date getOnlyDatePlusYear(Date date, int yearAdd) {
        DateTime dt = new DateTime(date);
        dt = dt.plusYears(yearAdd).dayOfMonth().roundFloorCopy();
        return dt.toDate();
    }

    public static Date getFirstDayOfMonth(Date date) {
        DateTime dt = new DateTime(date);
        dt = dt.dayOfMonth().withMinimumValue();
        return dt.toDate();
    }

    public static Date getLastDayOfMonth(Date date) {
        DateTime dt = new DateTime(date);
        dt = dt.dayOfMonth().withMaximumValue();
        return dt.toDate();
    }

    public static Date getMaxDate() {
        DateTime dt = new DateTime(9999,12,31,0,0,0,0, BuddhistChronology.getInstance());
        return dt.toDate();
    }

    public static Date getCurrentDateTH(){
        Date date = new Date();
        Date currentDate = convertToDateTH(date);

        return currentDate;
    }

    public static int daysBetween2Dates(Date date1, Date date2) {
        return Days.daysBetween(new DateTime(date1), new DateTime(date2)).getDays();
    }

    public static int monthBetween2DatesWithNoDate(Date date1, Date date2) {
        return Months.monthsBetween(new DateTime(date1).withDayOfMonth(1), new DateTime(date2).withDayOfMonth(1)).getMonths();
    }

    public static int checkDateDelete(Date startDate,Date endDate){
        if(daysBetween2Dates(new Date(), startDate) <= 0
                && daysBetween2Dates(new Date(), endDate) > 0 ){
            return -1;
        } else if(daysBetween2Dates(new Date(), endDate) < 0){
            return -2;
        } else {
            return 0;
        }
    }

    public static String validDateEdit(Date startDate,Date endDate) {
        String rtnValidDate = "0";
        if(startDate==null){
            rtnValidDate = "1";
            return rtnValidDate;
        }else if(endDate==null){
            rtnValidDate = "2";
            return rtnValidDate;
        }else if (daysBetween2Dates(startDate, endDate)<=0) {
            rtnValidDate = "4";
            return rtnValidDate;
        }else if (daysBetween2Dates(new Date(), endDate)<0) {
            rtnValidDate = "5";
            return rtnValidDate;
        }
        /*else if (Util.daysBetween2Dates(new Date(), interest.getStartDate())<=0
            && Util.daysBetween2Dates(new Date(), interest.getEndDate())>0) {
            rtnValidDate = "5";
            return rtnValidDate;
        }*/
        return rtnValidDate;
    }

    public static Calendar dateToCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static String parseDateToString(Date date, String dateFormat, Locale locale) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, locale);
        return sdf.format(date);
    }

    public static String parseDateToString(Date date, String dateFormat) {
        return parseDateToString(date, dateFormat, Locale.US);
    }

    public static String getFullDate(Date date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd MMMM yyyy HH:mm:ss")
                .withChronology(BuddhistChronology.getInstance()).withLocale(defaultLocale);
        DateTime dt = new DateTime(date);
        return dt.toString(formatter);
    }

    public static String getDateTimeStr(Date date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")
                .withChronology(BuddhistChronology.getInstance()).withLocale(defaultLocale);
        DateTime dt = new DateTime(date);
        return dt.toString(formatter);
    }

    public static String getDateStr(Date date) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy")
                .withChronology(BuddhistChronology.getInstance()).withLocale(new Locale("th", "TH"));
        DateTime dt = new DateTime(date);
        return dt.toString(formatter);
    }

    public static Date parseToDate(String dateString) {
        return parseToDate(dateString,defaultDateFormat);
    }

    public static Date parseToDate(String dateString,String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
        DateTime dt = formatter.withChronology(BuddhistChronology.getInstance()).withLocale(defaultLocale).parseDateTime(dateString);
        return dt.toDate();
    }

    public static Date parseToDate(String dateString,String dateFormat, Locale locale) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
        DateTime dt = formatter.withChronology(BuddhistChronology.getInstance()).withLocale(locale).parseDateTime(dateString);
        return dt.toDate();
    }

    public static List<String> getPreviousFiftyYearTH(){
        Date now = new Date();
        Calendar c = Calendar.getInstance(new Locale("th", "TH"));
        List<String> stringList = new ArrayList<String>();
        for (int i=0; i<50; i++) {
            Date d = getOnlyDatePlusYear(now, -i);
            c.setTime(d);
            int year = c.get(Calendar.YEAR);
            stringList.add(String.valueOf(year));
        }
        return stringList;
    }

    public static List<String> getPreviousHundredYearTH() {
        Date now = new Date();
        Calendar c = Calendar.getInstance(new Locale("th", "TH"));
        List<String> stringList = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            Date d = getOnlyDatePlusYear(now, -i);
            c.setTime(d);
            int year = c.get(Calendar.YEAR);
            stringList.add(String.valueOf(year));
        }
        return stringList;
    }
}