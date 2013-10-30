package com.clevel.selos.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class DateTimeUtilTest {
    private static Logger log = LoggerFactory.getLogger(DateTimeUtilTest.class);

    @Test
    public void testDateToString() throws Exception {
        log.info("testDateToString.");
        log.info("date: {}",DateTimeUtil.getDateTimeStr(new Date()));
        log.info("date: {}",DateTimeUtil.parseToDate("29/10/2556"));
    }
}
