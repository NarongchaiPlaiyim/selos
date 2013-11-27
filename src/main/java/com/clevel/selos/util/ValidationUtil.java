package com.clevel.selos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ValidationUtil {
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

    public static boolean isValueGreaterThanZero(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isValueGreaterEqualZero(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static boolean isValueLessThanZero(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    public static boolean isValueLessEqualZero(BigDecimal value) {
        return value.compareTo(BigDecimal.ZERO) <= 0;
    }

    public static boolean isValueEqualZero(BigDecimal value) {
        return BigDecimal.ZERO.compareTo(value) == 0;
    }

    public static boolean isValueEqual(BigDecimal value1, BigDecimal value2) {
        return value1.compareTo(value2) == 0;
    }

    public static boolean isGreaterThan(BigDecimal value, BigDecimal target) {
        return value.compareTo(target) > 0;
    }

    public static boolean isLessThan(BigDecimal value, BigDecimal target) {
        return value.compareTo(target) < 0;
    }
}
