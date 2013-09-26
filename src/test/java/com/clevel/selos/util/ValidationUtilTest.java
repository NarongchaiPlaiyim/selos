package com.clevel.selos.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class ValidationUtilTest {
    private static Logger log = LoggerFactory.getLogger(ValidationUtilTest.class);

    public ValidationUtilTest() {
    }

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    @Test
    public void testIsValueInRange() throws Exception {
        log.info("testIsValueInRange.");
        assertEquals("expected true.", true, ValidationUtil.isValueInRange(1, 10, 5));
        assertEquals("expected true.", true, ValidationUtil.isValueInRange(1, 10, 1));
        assertEquals("expected true.", true, ValidationUtil.isValueInRange(1, 10, 10));
        assertEquals("expected false.", false, ValidationUtil.isValueInRange(1, 10, 0));
        assertEquals("expected false.", false, ValidationUtil.isValueInRange(1, 10, 11));
        assertEquals("expected false.",false,ValidationUtil.isValueInRange(1,10,-1));
    }

    @Test
    public void testIsGreaterThan() throws Exception {
        log.info("testIsGreaterThan.");
        assertEquals("expected true.", true, ValidationUtil.isGreaterThan(10, 11));
        assertEquals("expected false.", false, ValidationUtil.isGreaterThan(10, 10));
        assertEquals("expected false.",false,ValidationUtil.isGreaterThan(10,9));
    }

    @Test
    public void testIsGreaterThanString() throws Exception{
        log.info("testIsGreaterThanString.");
        assertEquals("expected true.", true, ValidationUtil.isGreaterThan(5, "xxxxxx"));
        assertEquals("expected false.", false, ValidationUtil.isGreaterThan(5, "xxxxx"));
        assertEquals("expected true.",true,ValidationUtil.isGreaterThan(5,null));
        assertEquals("expected false.",false,ValidationUtil.isGreaterThan(5,""));
    }

    @Test
    public void testIsLessThan() throws Exception {
        log.info("testIsLessThan.");
        assertEquals("expected true.", true, ValidationUtil.isLessThan(10, 9));
        assertEquals("expected false.", false, ValidationUtil.isLessThan(10, 10));
        assertEquals("expected false.", false, ValidationUtil.isLessThan(10, 11));
    }

    //for NCB
    @Test
    public void testisEmpty() throws Exception {
        log.info("testisEmpty.");
        assertEquals("expected true.", true, ValidationUtil.isEmpty(null));
        assertEquals("expected true.", true, ValidationUtil.isEmpty(""));
        assertEquals("expected false.", false, ValidationUtil.isEmpty("null"));
    }

    @Test
    public void testisNull() throws Exception {
        log.info("testisEmpty.");
        assertEquals("expected true.", true, ValidationUtil.isNull(null));
        assertEquals("expected false.", false, ValidationUtil.isNull(""));
        assertEquals("expected true.", true, ValidationUtil.isNull("null"));
        assertEquals("expected true.", true, ValidationUtil.isNull("nulL"));
        assertEquals("expected true.", true, ValidationUtil.isNull("nUll"));

        String middlename = "123456789012345678901234567890";
        assertEquals("expected true.", true, !ValidationUtil.isNull(middlename)&& ValidationUtil.isGreaterThan(26, middlename));

        String middlename2 = "null";
        assertEquals("expected false.", false, !ValidationUtil.isNull(middlename2)&& ValidationUtil.isGreaterThan(26, middlename2));

        String middlename3 = null;
        assertEquals("expected false.", false, !ValidationUtil.isNull(middlename3)&& ValidationUtil.isGreaterThan(26, middlename3));

        String middlename4 = "12345678901234567890123456";
        assertEquals("expected false.", false, !ValidationUtil.isNull(middlename4)&& ValidationUtil.isGreaterThan(26, middlename4));
    }
}
