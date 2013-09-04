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
    public void testIsLessThan() throws Exception {
        log.info("testIsLessThan.");
        assertEquals("expected true.", true, ValidationUtil.isLessThan(10, 9));
        assertEquals("expected false.", false, ValidationUtil.isLessThan(10, 10));
        assertEquals("expected false.", false, ValidationUtil.isLessThan(10, 11));
    }
}
