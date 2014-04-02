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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateTimeUtil implements Serializable {
    private static Logger log = LoggerFactory.getLogger(DateTimeUtil.class);
    private static final Locale defaultLocale = new Locale("th", "EN");

    public static final String defaultDateFormat = "dd/MM/yyyy";

    private static final Locale THAI_LOCALE = new Locale("th", "TH");
    private static SimpleDateFormat viewDateFormatWT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.UK);

    public static String dateToStringWT(Date date){
        if(date != null)
            return viewDateFormatWT.format(date);
        else
            return "";
    }

    public static int compareDate(Date targetDate,Date referenceDate) {
        log.debug("compareDate() targetDate: {}, referenceDate: {}", targetDate, referenceDate);
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

    public static String convertToStringDDMMYYYY(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = simpleDateFormat.format(date);

        return dateString;
    }

    public static String convertToStringDDMMYYYY(Date date,Locale locale){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy",locale);
        String dateString = simpleDateFormat.format(date);

        return dateString;
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

    public static Date convertStringToDate(String date, String dateFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Date dateConvert = new Date();
        try {
            dateConvert = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            log.error("Error to parsing date. {}", date);
        }
        return dateConvert;
    }

    public static String convertDateToString(Date date, Locale locale, String dateFormat){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, locale);
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
        return convertToDateTH(new Date());
    }

    public static int daysBetween2Dates(Date date1, Date date2) {
        return Days.daysBetween(new DateTime(date1), new DateTime(date2)).getDays();
    }

    public static int monthBetween2DatesWithNoDate(Date date1, Date date2) {
        return Months.monthsBetween(new DateTime(date1).withDayOfMonth(1), new DateTime(date2).withDayOfMonth(1)).getMonths();
    }

    public static int monthBetween2Dates(Date date1, Date date2){
        return Months.monthsBetween(new DateTime(date1), new DateTime(date2)).getMonths();
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

    /**
     * Return:<br/>
     * 1: startDate is null<br/>
     * 2: endDate is null<br/>
     * 4: startDate is before or equal endDate<br/>
     * 5: endDate is after now
     * @param startDate
     * @param endDate
     * @return returnValidDateString
     */
    public static String validDateEdit(Date startDate,Date endDate) {
        String rtnValidDate = "0";
        if(startDate==null) {
            rtnValidDate = "1";
            return rtnValidDate;
        } else if(endDate==null) {
            rtnValidDate = "2";
            return rtnValidDate;
        } else if (daysBetween2Dates(startDate, endDate)<=0) {
            rtnValidDate = "4";
            return rtnValidDate;
        } else if (daysBetween2Dates(new Date(), endDate)<0) {
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

    public static int getDayOfDate(Date date) {
        int day;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        day = cal.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    public static int getMonthOfDate(Date date) {
        return dateToCalendar(date).get(Calendar.MONTH) + 1;
    }

    public static int getYearOfDate(Date date) {
        return dateToCalendar(date).get(Calendar.YEAR);
    }

    public static Date getMaxOfDate(Date date,Date date2) {
        if(compareDate(date,date2) < 0){
            return date;
        } else {
            return date2;
        }
    }

    public static String calYearMonth(Date date) {
        if(date != null){
            String yearMonth = "00Y 00M";
            Calendar year = Calendar.getInstance();
            year.setTime(date);
            Calendar today = Calendar.getInstance();
            if(year.after(today)){
                return yearMonth;
            }
            int y = today.get(Calendar.YEAR) - year.get(Calendar.YEAR);
            int m = today.get(Calendar.MONTH) - year.get(Calendar.MONTH);
            if(today.get(Calendar.DAY_OF_MONTH) < year.get(Calendar.DAY_OF_MONTH) || today.get(Calendar.DAY_OF_YEAR) < year.get(Calendar.DAY_OF_YEAR)){
                m--;
            }
            if(m < 0){
                y--;
                m = 12+m;
            }
            yearMonth = y+"Y "+m+"M";
            return yearMonth;
        } else {
            return "";
        }
    }

    public static int calMonth(Date date) {
        if(date != null){
            int month = 0;
            Calendar year = Calendar.getInstance();
            year.setTime(date);
            Calendar today = Calendar.getInstance();
            if(year.after(today)){
                return month;
            }
            int y = today.get(Calendar.YEAR) - year.get(Calendar.YEAR);
            int m = today.get(Calendar.MONTH) - year.get(Calendar.MONTH);
            if(today.get(Calendar.DAY_OF_MONTH) < year.get(Calendar.DAY_OF_MONTH) || today.get(Calendar.DAY_OF_YEAR) < year.get(Calendar.DAY_OF_YEAR)){
                m--;
            }
            if(m < 0){
                y--;
                m = 12+m;
            }
            int ym = y*12;
            int mm = ym+m;
            month = mm;
            return month;
        } else {
            return 0;
        }
    }

    public static Date getFirstDayOfMonthDatePlusOneYear(Date date){
        if(date != null){
            int month = dateToCalendar(date).get(Calendar.MONTH);
            int year = dateToCalendar(date).get(Calendar.YEAR);
            int nextYear = year+1;

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE,1);
            cal.set(Calendar.MONTH,month);
            cal.set(Calendar.YEAR,nextYear);
            return cal.getTime();
        } else {
            return null;
        }
    }
}