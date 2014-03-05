package com.clevel.selos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class ValidationUtil implements Serializable {
    private static Logger log = LoggerFactory.getLogger(ValidationUtil.class);

    public static boolean isValueInRange(int min, int max, int value) {
        return value >= min && value <= max;
    }

    public static boolean isGreaterThan(int max, int value) {
        return value > max;
    }

    public static boolean isLessThan(int min, int value) {
        return value < min;
    }

    public static boolean isValueEqual(int expected, int value) {
        return value == expected;
    }

    //for NCB
    public static boolean isEmpty(String field) {
        return (field == null) || "".equalsIgnoreCase(field.trim());
    }

    public static boolean isNull(String string) {
        if (string == null || "null".equals(string.toLowerCase().trim())) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValueInRange(int min, int max, String string) {
        if (!isNull(string)) {
            return (string.length() >= min) && (string.length() <= max);
        } else {
            return false;
        }
    }

    public static boolean isValueInRange(int min, int max, ArrayList arrayList) {
        if (null != arrayList) {
            return (arrayList.size() >= min) && (arrayList.size() <= max);
        } else {
            return false;
        }
    }

    public static boolean isGreaterThan(int max, String string) {
        return isNull(string) || string.length() > max;
    }

    public static boolean isNotNullAndGreaterThan(int max, String string) {
        if (string != null) {
            return string.length() > max;
        } else {
            return false;
        }
    }

    public static boolean isLessThan(int min, String string) {
        return isNull(string) || string.length() < min;
    }

    public static boolean isLessThan(int min, ArrayList arrayList) {
        if (arrayList != null) {
            return arrayList.size() < min;
        } else {
            return true;
        }
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    public static boolean isValueInRange(BigDecimal min, BigDecimal max, BigDecimal value) {
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }

    public static enum CompareMode{GREATER_THAN,GREATER_THAN_OR_EQUAL,EQUAL,LESS_THAN,LESS_THAN_OR_EQUAL}
    public static boolean isFirstCompareToSecond(BigDecimal first,BigDecimal second,CompareMode compareMode) {
        if (first==null || second==null ) {
            return false;
        }
        switch (compareMode) {
            case GREATER_THAN:
                return first.compareTo(second) == 1;
            case GREATER_THAN_OR_EQUAL:
                return first.compareTo(second) >= 0;
            case EQUAL:
                return first.compareTo(second) == 0;
            case LESS_THAN:
                return first.compareTo(second) == -1;
            case LESS_THAN_OR_EQUAL:
                return first.compareTo(second) <= 0;
            default:
                return false;
        }
    }

    public static boolean isValueCompareToZero(BigDecimal value,CompareMode compareMode) {
        if (value==null) {
            return false;
        }
        switch (compareMode) {
            case GREATER_THAN:
                return value.compareTo(BigDecimal.ZERO) == 1;
            case GREATER_THAN_OR_EQUAL:
                return value.compareTo(BigDecimal.ZERO) >= 0;
            case EQUAL:
                return value.compareTo(BigDecimal.ZERO) == 0;
            case LESS_THAN:
                return value.compareTo(BigDecimal.ZERO) == -1;
            case LESS_THAN_OR_EQUAL:
                return value.compareTo(BigDecimal.ZERO) <= 0;
            default:
                return false;
        }
    }

}
