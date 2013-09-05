package com.clevel.selos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
